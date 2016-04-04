package info;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.xml.parsers.ParserConfigurationException;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;

import com.google.common.io.Files;

import info.cloud.Cloud;
import info.cloud.CloudImageRepository;
import info.image.Image;
import info.image.WebImageRepository;

public class Main {

	public static void main(String[] args) throws ParserConfigurationException, IOException, JDOMException {
		// Criamos um objeto SAXBuilder
		// para ler o arquivo
		SAXBuilder sb = new SAXBuilder();

		SystemProperties.getInstance().load(new FileInputStream(new File("Properties.properties")));

		Document docImages = sb.build(new File("images.xml"));
		Document docVOs = sb.build(new File("VO.xml"));
		Document docVOsImages = sb.build(new File("VOs_Images.xml"));

		File deduped_images = new File("final_result.txt");

		Map<String, Image> images = readImages(docImages.getRootElement());
		List<Cloud> clouds = readVOs(docVOs.getRootElement(), docVOsImages.getRootElement(), images);
		Map<Long, List<Workload>> workloads = readWorkload(images);

		readDedupedImages(deduped_images, images);
		Controller controller = new Controller(workloads);

		GlobalClock gc = GlobalClock.getInstance();
		
		gc.addObserver(controller);
		for (Cloud c : clouds) {
			gc.addObserver(c);
			controller.addObserver(c);
		}
		gc.run();
	}

	private static void readDedupedImages(File deduped_images, Map<String, Image> images) throws IOException {
		ImagesSize deduped_data = ImagesSize.getInstance();
		List<String> lines = Files.readLines(deduped_images, Charset.defaultCharset());
		for (String line : lines) {
			Map<Integer, Double> info = new HashMap<Integer, Double>();
			String[] imagesNames = line.split(":");
			String[] names = imagesNames[0].split(" ");
			int size = Integer.valueOf(imagesNames[1].trim());
			String imagesIDs = "";
			imagesIDs.trim();
			for (String name : names) {
				info.put(images.get(name).getID(), new Double(size));
			}
			deduped_data.addInformation(info);
		}

	}

	private static Map<Long, List<Workload>> readWorkload(Map<String, Image> images) throws IOException {
		List<String> lines = Files.readLines(new File("result_workload.txt"), Charset.defaultCharset());
		Map<Long, List<Workload>> workload = new HashMap<Long, List<Workload>>();
		for (String line : lines) {
			String[] split = line.split(" ");
			// 1 é a posicao do tempo de inicio do pedido, 2 é o tempo de
			// execucao, 3 é a nuvem
			if (workload.get(Long.valueOf(split[1])) == null) {
				workload.put(Long.valueOf(split[1]), new ArrayList<Workload>());
			}
			List<Integer> ids = new ArrayList<Integer>();
			for (String name : images.keySet()) {
				ids.add(images.get(name).getID());
			}
			int id = new Random().nextInt(25);
			if (id == 0) {
				id = 1;
			}
			workload.get(Long.valueOf(split[1])).add(new Workload(Integer.valueOf(split[0]), Integer.valueOf(split[3]),
					Integer.valueOf(split[1]), Integer.valueOf(split[2]), id));
		}
		return workload;
	}

	private static Map<String, Image> readImages(Element doc) {
		Map<String, Image> images = new HashMap<String, Image>();
		List<Element> nodeImages = doc.getChildren("image");
		for (Element e : nodeImages) {
			Image image = new Image(e.getAttributeValue("name"), e.getAttributeValue("os"),
					e.getAttributeValue("os_version"), Integer.parseInt(e.getAttributeValue("id")),
					Integer.parseInt(e.getAttributeValue("size")));
			images.put(e.getAttributeValue("name"), image);
		}
		return images;
	}

	private static List<Cloud> readVOs(Element vos, Element docVOsImages, Map<String, Image> images) {
		List<Cloud> clouds = new ArrayList<Cloud>();
		List<Element> vosList = vos.getChildren("site");
		for (Element vo : vosList) {
			int cloudID = Integer.parseInt(vo.getAttributeValue("id"));
			String cloudName = vo.getAttributeValue("name");
			Resources resources = new Resources(
					Integer.parseInt(SystemProperties.getInstance().getPropertyValue("vcpu")),
					Integer.parseInt(SystemProperties.getInstance().getPropertyValue("ram")),
					Double.parseDouble(SystemProperties.getInstance().getPropertyValue("disk")),
					Integer.parseInt(SystemProperties.getInstance().getPropertyValue("bandwidth")));
			CloudImageRepository repository = new CloudImageRepository(cloudID, 200.0);
			Cloud cloud = new Cloud(cloudID, cloudName, resources, repository);
			for (Image img : getCloudImages(docVOsImages, cloud.getID(), images)) {
				if (img != null) {
					repository.addImage(cloudID, img, 0L);
					clouds.add(cloud);
				}
			}
		}
		WebImageRepository.getInstance().setImages(new ArrayList<Image>(images.values()));
		return clouds;
	}

	private static List<Image> getCloudImages(Element docVOsImages, int id, Map<String, Image> images) {
		List<Image> voImages = new ArrayList<Image>();
		List<Element> list = docVOsImages.getChildren("site");
		for (Element s : list) {
			if (Integer.valueOf(s.getAttributeValue("id")) == id) {
				List<Element> imgs = s.getChildren("image");
				for (Element img : imgs) {
					int imageId = Integer.valueOf(img.getText());
					voImages.add(getImageByID(imageId, images));
				}
			}
		}
		return voImages;
	}

	private static Image getImageByID(int imageId, Map<String, Image> images) {
		for (String name : images.keySet()) {
			if (images.get(name).getID() == imageId) {
				return images.get(name);
			}
		}
		return null;
	}

}
