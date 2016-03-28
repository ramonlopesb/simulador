package info;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import info.cloud.Cloud;
import info.cloud.CloudImageRepository;
import info.image.Image;
import info.image.WebImageRepository;

public class Main {

	public static void main(String[] args) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();

		Document docImages = db.parse(new File("images.xml"));
		Document docVOs = db.parse(new File("VO.xml"));
		Document docVOsImages = db.parse(new File("VOs_Images.xml"));

		File deduped_images = new File("Deduped_Images.txt");
		readDedupedImages(deduped_images);

		List<Image> images = readImages(docImages);
		List<Cloud> clouds = readVOs(docVOs, docVOsImages, images);
		Map<Long, List<Workload>> workloads = readWorkload();

		Controller controller = new Controller(workloads);
		
		GlobalClock gc = GlobalClock.getInstance();
		
		gc.addObserver(controller);
		for (Cloud c: clouds) {
			gc.addObserver(c);
		}
	}

	private static void readDedupedImages(File deduped_images) throws IOException {
		ImagesSize deduped_data = ImagesSize.getInstance();
		Map<Integer, Double> info = new HashMap<Integer, Double>();

		FileReader fReader = new FileReader(deduped_images);
		BufferedReader buffer = new BufferedReader(fReader);
		String line = buffer.readLine();

		while (line != null) {
			String[] images = line.split(" ");
			for (int i = 0; i < images.length; i += 2) {
				info.put(Integer.parseInt(images[i]), Double.parseDouble(images[i + 1]));
			}
		}
		buffer.close();

		deduped_data.addInformation(info);
	}

	private static Map<Long, List<Workload>> readWorkload() {
		File f = new File("Workload.txt");
		return null;
	}

	private static List<Image> readImages(Document doc) {
		List<Image> images = new ArrayList<Image>();
		doc.getDocumentElement().normalize();
		NodeList list = doc.getElementsByTagName("image");
		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) item;
				Image image = new Image(e.getAttribute("name"), e.getAttribute("os"), e.getAttribute("os_version"),
						Integer.parseInt(e.getAttribute("id")), Integer.parseInt(e.getAttribute("size")));
				images.add(image);
			}
		}
		return images;
	}

	private static List<Cloud> readVOs(Document vos, Document docVOsImages, List<Image> images) {
		List<Cloud> clouds = new ArrayList<Cloud>();
		vos.getDocumentElement().normalize();
		NodeList list = vos.getElementsByTagName("site");
		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) item;
				Resources resources = new Resources(Integer.parseInt(e.getAttribute("vcpu")),
						Integer.parseInt(e.getAttribute("ram")), Double.parseDouble(e.getAttribute("disk")),
						Integer.parseInt(e.getAttribute("bandwidht")));
				CloudImageRepository repository = new CloudImageRepository(Integer.parseInt(e.getAttribute("id")),
						200.0);
				Cloud cloud = new Cloud(Integer.parseInt(e.getAttribute("id")), e.getAttribute("name"), resources,
						repository);
				for (Image img : getCloudImages(docVOsImages, cloud.getID(), images)) {
					repository.addImage(Integer.parseInt(e.getAttribute("id")), img, 0L);
				}
				clouds.add(cloud);
			}
		}
		WebImageRepository.getInstance().setImages(images);
		return clouds;
	}

	private static List<Image> getCloudImages(Document docVOsImages, int id, List<Image> images) {
		List<Image> voImages = new ArrayList<Image>();
		docVOsImages.getDocumentElement().normalize();
		NodeList list = docVOsImages.getElementsByTagName("site");
		for (int i = 0; i < list.getLength(); i++) {
			Node item = list.item(i);
			if (item.getNodeType() == Node.ELEMENT_NODE) {
				Element e = (Element) item;
				if (Integer.parseInt(e.getAttribute("id")) == id) {
					NodeList imgs = e.getElementsByTagName("image");
					for (int j = 0; j < imgs.getLength(); j++) {
						Node img = imgs.item(j);
						if (img.getNodeType() == Node.ELEMENT_NODE) {
							int imageId = Integer.parseInt(((Element) img).getTextContent());
							voImages.add(getImageByID(imageId, images));
						}
					}
				}
			}
		}
		return voImages;
	}

	private static Image getImageByID(int imageId, List<Image> images) {
		for (Image img : images) {
			if (img.getID() == imageId) {
				return img;
			}
		}
		return null;
	}

}
