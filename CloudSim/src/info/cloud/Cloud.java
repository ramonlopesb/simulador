package info.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import com.google.common.primitives.Ints;

import info.GlobalClock;
import info.LogWritter;
import info.Request;
import info.Resources;
import info.VirtualMachine;
import info.image.WebImageRepository;;

public class Cloud implements Observer {

	private int id;
	private String name;
	private Resources resources;
	private CloudScoreBoard scoreBoard = CloudScoreBoard.getInstance();

	private Long time;

	private List<Request> completedRequests;

	// <pedido, tempo>
	private Map<Request, Long> pendingRequests;

	// <tempo, <id da cloud que requisitou, id da imagem>>
	private Map<Long, Map<Integer, Integer>> imagesToDownload;

	// <id da imagem, vm>
	private Map<Integer, List<VirtualMachine>> imageThreads;

	// o cache system é responsável pelo repositório de imagens
	private CacheSystem cacheSystem;

	public Cloud(int id, String name, Resources resources, CloudImageRepository repository) {
		this.id = id;
		this.name = name;
		this.resources = resources;
		this.cacheSystem = new CacheSystem(id, repository);
		this.completedRequests = new ArrayList<Request>();
		this.pendingRequests = new HashMap<Request, Long>();
		this.imagesToDownload = new HashMap<Long, Map<Integer, Integer>>();
		this.imageThreads = new HashMap<Integer, List<VirtualMachine>>();
	}

	public int getID() {
		return this.id;
	}

	public String getCloudName() {
		return this.name;
	}

	public synchronized void answer(List<Request> requests) {
		while (requests.size() > 0) {
			this.cacheSystem.verifyContention(getRequestsImagesIDs(requests));
			Request r = requests.get(0);
			if (this.resources.canAnswerRequest(r)) {
				if (this.cacheSystem.contains(r.getImageID())) {
					// se a requisição ainda está pendente, atendê-la
					if (r.isPending()) {
						r.setPending(false);
						resources.allocate(r.getVCPUs(), r.getRam(), r.getDiskSize());

						// verifica se a imagem já tem um dono
						int imageOwnerID = this.cacheSystem.getImageOwnerID(r.getImageID());

						if (imageThreads.get(r.getImageID()) == null) {
							imageThreads.put(r.getImageID(), new ArrayList<VirtualMachine>());
						} else {
							boolean hasOneAlive = false;
							for (VirtualMachine run : imageThreads.get(r.getImageID())) {
								if (run.getEndingTime() < time) {
									hasOneAlive = true;
									break;
								}
							}

							// se não tem mais donos, adiciona
							if (!hasOneAlive) {
								if (r.getFromCloudID() != imageOwnerID) {
									this.cacheSystem.setImageOwnerID(r.getFromCloudID(), r.getImageID());
								}
							}
						}
						System.out.println(time + " - " + "Nuvem: " + this.id + ": Iniciando a VM " + r.getImageID()
								+ " do pedido " + r.getRequestID());
						LogWritter.getInstance().write(time + " - " + "Nuvem: " + this.id + ": Iniciando a VM "
								+ r.getImageID() + " do pedido " + r.getRequestID());
						VirtualMachine vm = new VirtualMachine(r);
						vm.run();
						vm.addObserver(this);
						GlobalClock.getInstance().addObserver(vm);
						imageThreads.get(r.getImageID()).add(vm);
						this.cacheSystem.setImageInsertionTime(r.getImageID(), time);
					}
				} else {
					long timeToDownload = resources.timeToDownload(r.getImageID());
					// agendar o pedido para após o tempo de download
					r.setStartingTime(r.getStartTime() + timeToDownload);
					System.out.println(time + " - " + "Nuvem: " + this.id + ": Adiando o pedido " + r.getRequestID()
							+ " para o tempo " + r.getStartTime() + " por falta de imagem.");
					LogWritter.getInstance().write(time + " - " + "Nuvem: " + this.id + ": Adiando o pedido "
							+ r.getRequestID() + " para o tempo " + r.getStartTime() + " por falta de imagem.");
					this.pendingRequests.put(r, r.getStartTime() + timeToDownload);
					if (this.imagesToDownload.get(r.getStartTime() + timeToDownload) == null) {
						this.imagesToDownload.put(r.getStartTime() + timeToDownload, new HashMap<Integer, Integer>());
					}
					this.imagesToDownload.get(r.getStartTime() + timeToDownload).put(r.getFromCloudID(),
							r.getImageID());
				}
			} else {
				// tentar novamente em 10s
				System.out.println(time + " - " + "Nuvem: " + this.id + ": Adiando o pedido " + r.getRequestID()
						+ " para o tempo " + r.getStartTime() + " por falta de recursos.");
				LogWritter.getInstance().write(time + " - " + "Nuvem: " + this.id + ": Adiando o pedido "
						+ r.getRequestID() + " para o tempo " + r.getStartTime() + " por falta de recursos.");
				r.setStartingTime(r.getStartTime() + 10);
				this.pendingRequests.put(r, r.getStartTime() + 10);
			}
			requests.remove(0);
		}
	}

	private int[] getRequestsImagesIDs(List<Request> requests) {
		Set<Integer> a = new HashSet<Integer>();
		for (Request r : requests) {
			a.add(r.getImageID());
		}
		return Ints.toArray(a);
	}

	@Override
	public void update(Observable o, final Object arg) {
		if (arg instanceof Request) { // atualizando um pedido atendido
			synchronized (arg) {
				Request r = (Request) arg;
				long score = r.getDurationTime() * WebImageRepository.getInstance().getImage(r.getImageID()).getSize();
				System.out.println(time + " - " + "Nuvem: " + this.id + ": Pontuando a quantidade de " + score
						+ " por ter iniciado uma VM da imagem " + r.getImageID() + " à nuvem " + r.getFromCloudID());
				LogWritter.getInstance()
						.write(time + " - " + "Nuvem: " + this.id + ": Pontuando a quantidade de " + score
								+ " por ter iniciado uma VM da imagem " + r.getImageID() + " à nuvem "
								+ r.getFromCloudID());
				this.completedRequests.add(r);
				this.resources.deallocate(r.getVCPUs(), r.getRam(), r.getDiskSize());

				// pontua
				scoreBoard.scoreByCloud(this.id, r.getFromCloudID(), score);
				scoreBoard.scoreByImage(this.id, r.getImageID(), r.getDurationTime());

				this.pendingRequests.remove(r);
			}
		} else if (arg instanceof Long) { // atualizando uma imagem que está
											// sendo baixada
			new Thread(new Runnable() {
				@Override
				public void run() {
					time = (Long) arg;
					if (imagesToDownload.get(time) != null) {
						Map<Integer, Integer> images = imagesToDownload.get(time);
						for (Integer cloudID : images.keySet()) {
							cacheSystem.add(cloudID, WebImageRepository.getInstance().getImage(images.get(cloudID)),
									time);
						}
					}
					if (!pendingRequests.isEmpty()) {
						List<Request> requests = new ArrayList<Request>();
						for (Request r : pendingRequests.keySet()) {
							if (pendingRequests.get(r) == time) {
								requests.add(r);
							}
						}
						answer(requests);
					}
				}
			}).start();
		} else if (arg instanceof List) { // atualizando novos pedidos
			final List<Request> cr = (List<Request>) arg;
			System.out.println("Chegaram na cloud");
			new Thread(new Runnable() {
				@Override
				public void run() {
					answer(cr);
				}
			}).start();
		}
	}

}
