package extraction;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.io.Files;

public class DedupResultExtraction {

	static Map<String, Integer> individual;
	static Map<String, Integer> finalRes;
	static Map<String, Integer> intersection;

	public static void main(String[] args) throws IOException {
		File output = new File("/home/stack/git/CloudSim/Deduped_Images.txt");
		List<String> lines = Files.readLines(output, Charset.defaultCharset());
		individual = new HashMap<String, Integer>();
		finalRes = new HashMap<String, Integer>();

		for (String line : lines) {
			String[] split = line.split(" ");
			individual.put(split[0].trim(), Integer.valueOf(split[2]));
		}

		output = new File("/home/stack/git/CloudSim/resultado_dois_a_dois.txt");
		lines = Files.readLines(output, Charset.defaultCharset());
		List<String> out = new ArrayList<String>();

		for (String line : lines) {
			if (line.contains("Adicionando ") || line.contains("sdfs:/etc/sdfs/")) {
				out.add(line);
			}
		}

		intersection = new HashMap<String, Integer>();

		for (String image : individual.keySet()) {
			finalRes.put(image, individual.get(image));
		}

		int pos = 0;
		while (pos < out.size()) {
			String[] split = out.get(pos).split(" ");
			String[] image1path = split[1].split("/");
			String image1 = image1path[image1path.length - 1].trim();

			String[] image2path = split[3].split("/");
			String image2 = image2path[image2path.length - 1].trim();

			Integer result = Integer.valueOf(out.get(pos + 1).split(" ")[2].replace("M", ""));

			intersection.put(image1 + " " + image2, individual.get(image1) + individual.get(image2) - result);
			finalRes.put(image1 + " " + image2, result);
			pos += 2;
		}

		List<String> imagens = new ArrayList<String>(individual.keySet());

		for (int a = 0; a < imagens.size(); a++) {
			for (int b = a + 1; b < imagens.size(); b++) {
				for (int c = b + 1; c < imagens.size(); c++) {
					finalRes.put(imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c),
							(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c))
									+ intersect(false, imagens.get(a), imagens.get(b), imagens.get(c))) / 2);
					for (int d = c + 1; d < imagens.size(); d++) {
						finalRes.put(
								imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c) + " " + imagens.get(d),
								(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c), imagens.get(d))
										+ intersect(false, imagens.get(a), imagens.get(b), imagens.get(c),
												imagens.get(d)))
										/ 2);
						for (int e = d + 1; e < imagens.size(); e++) {
							finalRes.put(
									imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c) + " " + imagens.get(d)
											+ " " + imagens.get(e),
									(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c), imagens.get(d),
											imagens.get(e))
											+ intersect(false, imagens.get(a), imagens.get(b), imagens.get(c),
													imagens.get(d), imagens.get(e)))
											/ 2);
							for (int f = e + 1; f < imagens.size(); f++) {
								finalRes.put(
										imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c) + " "
												+ imagens.get(d) + " " + imagens.get(e) + " " + imagens.get(f),
										(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c), imagens.get(d),
												imagens.get(e), imagens.get(f))
												+ intersect(false, imagens.get(a), imagens.get(b), imagens.get(c),
														imagens.get(d), imagens.get(e), imagens.get(f)))
												/ 2);
								for (int g = f + 1; g < imagens.size(); g++) {
									finalRes.put(
											imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c) + " "
													+ imagens.get(d) + " " + imagens.get(e) + " " + imagens.get(f) + " "
													+ imagens.get(g),
											(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c),
													imagens.get(d), imagens.get(e), imagens.get(f), imagens.get(g))
													+ intersect(false, imagens.get(a), imagens.get(b), imagens.get(c),
															imagens.get(d), imagens.get(e), imagens.get(f),
															imagens.get(g)))
													/ 2);
									for (int h = g + 1; h < imagens.size(); h++) {
										finalRes.put(
												imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c) + " "
														+ imagens.get(d) + " " + imagens.get(e) + " " + imagens.get(f)
														+ " " + imagens.get(g) + " " + imagens.get(h),
												(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c),
														imagens.get(d), imagens.get(e), imagens.get(f), imagens.get(g),
														imagens.get(h))
														+ intersect(false, imagens.get(a), imagens.get(b),
																imagens.get(c), imagens.get(d), imagens.get(e),
																imagens.get(f), imagens.get(g), imagens.get(h)))
														/ 2);
										for (int i = h + 1; i < imagens.size(); i++) {
											finalRes.put(
													imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c) + " "
															+ imagens.get(d) + " " + imagens.get(e) + " "
															+ imagens.get(f) + " " + imagens.get(g) + " "
															+ imagens.get(h) + " " + imagens.get(i),
													(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c),
															imagens.get(d), imagens.get(e), imagens.get(f),
															imagens.get(g), imagens.get(h), imagens.get(i))
															+ intersect(false, imagens.get(a), imagens.get(b),
																	imagens.get(c), imagens.get(d), imagens.get(e),
																	imagens.get(f), imagens.get(g), imagens.get(h),
																	imagens.get(i)))
															/ 2);
											for (int j = i + 1; j < imagens.size(); j++) {
												finalRes.put(
														imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c)
																+ " " + imagens.get(d) + " " + imagens.get(e) + " "
																+ imagens.get(f) + " " + imagens.get(g) + " "
																+ imagens.get(h) + " " + imagens.get(i) + " "
																+ imagens
																		.get(j),
														(intersect(true, imagens.get(a), imagens.get(b), imagens.get(c),
																imagens.get(d), imagens.get(e), imagens.get(f),
																imagens.get(g), imagens.get(h), imagens.get(i),
																imagens.get(j))
																+ intersect(false, imagens.get(a), imagens.get(b),
																		imagens.get(c), imagens.get(d), imagens.get(e),
																		imagens.get(f), imagens.get(g), imagens.get(h),
																		imagens.get(i), imagens.get(j)))
																/ 2);
												for (int k = j + 1; k < imagens.size(); k++) {
													finalRes.put(
															imagens.get(a) + " " + imagens.get(b) + " " + imagens.get(c)
																	+ " " + imagens.get(d) + " " + imagens.get(e) + " "
																	+ imagens.get(f) + " " + imagens.get(g) + " "
																	+ imagens.get(h) + " " + imagens.get(i) + " "
																	+ imagens.get(j) + " " + imagens.get(k),
															(intersect(true, imagens.get(a), imagens.get(b),
																	imagens.get(c), imagens.get(d), imagens.get(e),
																	imagens.get(f), imagens.get(g), imagens.get(h),
																	imagens.get(i), imagens.get(j), imagens.get(k))
																	+ intersect(false, imagens.get(a), imagens.get(b),
																			imagens.get(c), imagens.get(d),
																			imagens.get(e), imagens.get(f),
																			imagens.get(g), imagens.get(h),
																			imagens.get(i), imagens.get(j),
																			imagens.get(k)))
																	/ 2);
													for (int l = k + 1; l < imagens.size(); l++) {
														finalRes.put(
																imagens.get(a) + " " + imagens.get(b) + " "
																		+ imagens.get(c) + " " + imagens.get(d) + " "
																		+ imagens.get(e) + " " + imagens.get(f) + " "
																		+ imagens.get(g) + " " + imagens.get(h) + " "
																		+ imagens.get(i) + " " + imagens.get(j) + " "
																		+ imagens.get(k) + " " + imagens.get(l),
																(intersect(true, imagens.get(a), imagens.get(b),
																		imagens.get(c), imagens.get(d), imagens.get(e),
																		imagens.get(f), imagens.get(g), imagens.get(h),
																		imagens.get(i), imagens.get(j), imagens.get(k),
																		imagens.get(l)) + intersect(false, imagens.get(a), imagens.get(b),
																				imagens.get(c), imagens.get(d), imagens.get(e),
																				imagens.get(f), imagens.get(g), imagens.get(h),
																				imagens.get(i), imagens.get(j), imagens.get(k),
																				imagens.get(l))) / 2);
														for (int m = l + 1; m < imagens.size(); m++) {
															finalRes.put(imagens.get(a) + " " + imagens.get(b) + " "
																	+ imagens.get(c) + " " + imagens.get(d) + " "
																	+ imagens.get(e) + " " + imagens.get(f) + " "
																	+ imagens.get(g) + " " + imagens.get(h) + " "
																	+ imagens.get(i) + " " + imagens.get(j) + " "
																	+ imagens.get(k) + " " + imagens.get(l) + " "
																	+ imagens.get(m),
																	(intersect(true, imagens.get(a), imagens.get(b),
																			imagens.get(c), imagens.get(d),
																			imagens.get(e), imagens.get(f),
																			imagens.get(g), imagens.get(h),
																			imagens.get(i), imagens.get(j),
																			imagens.get(k), imagens.get(l),
																			imagens.get(m)) + intersect(false, imagens.get(a), imagens.get(b),
																					imagens.get(c), imagens.get(d),
																					imagens.get(e), imagens.get(f),
																					imagens.get(g), imagens.get(h),
																					imagens.get(i), imagens.get(j),
																					imagens.get(k), imagens.get(l),
																					imagens.get(m))) / 2);
															for (int n = m + 1; n < imagens.size(); n++) {
																finalRes.put(imagens.get(a) + " " + imagens.get(b) + " "
																		+ imagens.get(c) + " " + imagens.get(d) + " "
																		+ imagens.get(e) + " " + imagens.get(f) + " "
																		+ imagens.get(g) + " " + imagens.get(h) + " "
																		+ imagens.get(i) + " " + imagens.get(j) + " "
																		+ imagens.get(k) + " " + imagens.get(l) + " "
																		+ imagens.get(m) + " " + imagens.get(n),
																		(intersect(true, imagens.get(a), imagens.get(b),
																				imagens.get(c), imagens.get(d),
																				imagens.get(e), imagens.get(f),
																				imagens.get(g), imagens.get(h),
																				imagens.get(i), imagens.get(j),
																				imagens.get(k), imagens.get(l),
																				imagens.get(m), imagens.get(n)) + intersect(false, imagens.get(a), imagens.get(b),
																						imagens.get(c), imagens.get(d),
																						imagens.get(e), imagens.get(f),
																						imagens.get(g), imagens.get(h),
																						imagens.get(i), imagens.get(j),
																						imagens.get(k), imagens.get(l),
																						imagens.get(m), imagens.get(n))) / 2);
																for (int o = n + 1; o < imagens.size(); o++) {
																	finalRes.put(
																			imagens.get(a) + " " + imagens.get(b) + " "
																					+ imagens.get(c) + " "
																					+ imagens.get(d) + " "
																					+ imagens.get(e) + " "
																					+ imagens.get(f) + " " + imagens
																							.get(g)
																					+ " " + imagens.get(h) + " "
																					+ imagens.get(i) + " "
																					+ imagens.get(j) + " "
																					+ imagens.get(k) + " "
																					+ imagens.get(l) + " "
																					+ imagens.get(m) + " "
																					+ imagens.get(n) + " "
																					+ imagens.get(o),
																			(intersect(true, imagens.get(a), imagens.get(b),
																					imagens.get(c), imagens.get(d),
																					imagens.get(e), imagens.get(f),
																					imagens.get(g), imagens.get(h),
																					imagens.get(i), imagens.get(j),
																					imagens.get(k), imagens.get(l),
																					imagens.get(m), imagens.get(n),
																					imagens.get(o)) + intersect(false, imagens.get(a), imagens.get(b),
																							imagens.get(c), imagens.get(d),
																							imagens.get(e), imagens.get(f),
																							imagens.get(g), imagens.get(h),
																							imagens.get(i), imagens.get(j),
																							imagens.get(k), imagens.get(l),
																							imagens.get(m), imagens.get(n),
																							imagens.get(o))) / 2);
																	for (int p = o + 1; p < imagens.size(); p++) {
																		finalRes.put(
																				imagens.get(a) + " " + imagens.get(b)
																						+ " " + imagens.get(c) + " "
																						+ imagens.get(d) + " "
																						+ imagens.get(e) + " "
																						+ imagens.get(f) + " "
																						+ imagens.get(g) + " " + imagens
																								.get(h)
																						+ " " + imagens.get(i) + " "
																						+ imagens.get(j) + " "
																						+ imagens.get(k) + " "
																						+ imagens.get(l) + " "
																						+ imagens.get(m) + " "
																						+ imagens.get(n) + " "
																						+ imagens.get(o) + " "
																						+ imagens.get(p),
																				(intersect(true, imagens.get(a),
																						imagens.get(b), imagens.get(c),
																						imagens.get(d), imagens.get(e),
																						imagens.get(f), imagens.get(g),
																						imagens.get(h), imagens.get(i),
																						imagens.get(j), imagens.get(k),
																						imagens.get(l), imagens.get(m),
																						imagens.get(n), imagens.get(o),
																						imagens.get(p)) + intersect(false, imagens.get(a),
																								imagens.get(b), imagens.get(c),
																								imagens.get(d), imagens.get(e),
																								imagens.get(f), imagens.get(g),
																								imagens.get(h), imagens.get(i),
																								imagens.get(j), imagens.get(k),
																								imagens.get(l), imagens.get(m),
																								imagens.get(n), imagens.get(o),
																								imagens.get(p))) / 2);
																		for (int q = p + 1; q < imagens.size(); q++) {
																			finalRes.put(
																					imagens.get(a) + " "
																							+ imagens.get(b) + " "
																							+ imagens.get(c) + " "
																							+ imagens.get(d) + " "
																							+ imagens.get(e) + " "
																							+ imagens.get(f) + " "
																							+ imagens.get(g) + " "
																							+ imagens.get(h) + " "
																							+ imagens.get(i) + " "
																							+ imagens.get(j) + " "
																							+ imagens.get(k) + " "
																							+ imagens.get(l) + " "
																							+ imagens.get(m) + " "
																							+ imagens.get(n) + " "
																							+ imagens.get(o) + " "
																							+ imagens.get(p) + " "
																							+ imagens.get(q),
																					(intersect(true, imagens.get(a),
																							imagens.get(b),
																							imagens.get(c),
																							imagens.get(d),
																							imagens.get(e),
																							imagens.get(f),
																							imagens.get(g),
																							imagens.get(h),
																							imagens.get(i),
																							imagens.get(j),
																							imagens.get(k),
																							imagens.get(l),
																							imagens.get(m),
																							imagens.get(n),
																							imagens.get(o),
																							imagens.get(p),
																							imagens.get(q)) + intersect(false, imagens.get(a),
																									imagens.get(b),
																									imagens.get(c),
																									imagens.get(d),
																									imagens.get(e),
																									imagens.get(f),
																									imagens.get(g),
																									imagens.get(h),
																									imagens.get(i),
																									imagens.get(j),
																									imagens.get(k),
																									imagens.get(l),
																									imagens.get(m),
																									imagens.get(n),
																									imagens.get(o),
																									imagens.get(p),
																									imagens.get(q))) / 2);
																			for (int r = q + 1; r < imagens
																					.size(); r++) {
																				finalRes.put(
																						imagens.get(a) + " "
																								+ imagens.get(b) + " "
																								+ imagens.get(c) + " "
																								+ imagens.get(d) + " "
																								+ imagens.get(e) + " "
																								+ imagens.get(f) + " "
																								+ imagens.get(g) + " "
																								+ imagens.get(h) + " "
																								+ imagens.get(i) + " "
																								+ imagens.get(j) + " "
																								+ imagens.get(k) + " "
																								+ imagens.get(l) + " "
																								+ imagens.get(m) + " "
																								+ imagens.get(n) + " "
																								+ imagens.get(o) + " "
																								+ imagens.get(p) + " "
																								+ imagens.get(q) + " "
																								+ imagens.get(r),
																						(intersect(true, imagens.get(a),
																								imagens.get(b),
																								imagens.get(c),
																								imagens.get(d),
																								imagens.get(e),
																								imagens.get(f),
																								imagens.get(g),
																								imagens.get(h),
																								imagens.get(i),
																								imagens.get(j),
																								imagens.get(k),
																								imagens.get(l),
																								imagens.get(m),
																								imagens.get(n),
																								imagens.get(o),
																								imagens.get(p),
																								imagens.get(q),
																								imagens.get(r)) + intersect(false, imagens.get(a),
																										imagens.get(b),
																										imagens.get(c),
																										imagens.get(d),
																										imagens.get(e),
																										imagens.get(f),
																										imagens.get(g),
																										imagens.get(h),
																										imagens.get(i),
																										imagens.get(j),
																										imagens.get(k),
																										imagens.get(l),
																										imagens.get(m),
																										imagens.get(n),
																										imagens.get(o),
																										imagens.get(p),
																										imagens.get(q),
																										imagens.get(r))) / 2);
																				for (int s = r + 1; s < imagens
																						.size(); s++) {
																					finalRes.put(
																							imagens.get(a) + " "
																									+ imagens.get(b)
																									+ " "
																									+ imagens.get(c)
																									+ " " + imagens
																											.get(d)
																							+ " " + imagens.get(e) + " "
																							+ imagens.get(f) + " "
																							+ imagens.get(g) + " "
																							+ imagens.get(h) + " "
																							+ imagens.get(i) + " "
																							+ imagens.get(j) + " "
																							+ imagens.get(k) + " "
																							+ imagens.get(l) + " "
																							+ imagens.get(m) + " "
																							+ imagens.get(n) + " "
																							+ imagens.get(o) + " "
																							+ imagens.get(p) + " "
																							+ imagens.get(q) + " "
																							+ imagens.get(r) + " "
																							+ imagens.get(s),
																							(intersect(true, imagens.get(a),
																									imagens.get(b),
																									imagens.get(c),
																									imagens.get(d),
																									imagens.get(e),
																									imagens.get(f),
																									imagens.get(g),
																									imagens.get(h),
																									imagens.get(i),
																									imagens.get(j),
																									imagens.get(k),
																									imagens.get(l),
																									imagens.get(m),
																									imagens.get(n),
																									imagens.get(o),
																									imagens.get(p),
																									imagens.get(q),
																									imagens.get(r),
																									imagens.get(s)) + intersect(false, imagens.get(a),
																											imagens.get(b),
																											imagens.get(c),
																											imagens.get(d),
																											imagens.get(e),
																											imagens.get(f),
																											imagens.get(g),
																											imagens.get(h),
																											imagens.get(i),
																											imagens.get(j),
																											imagens.get(k),
																											imagens.get(l),
																											imagens.get(m),
																											imagens.get(n),
																											imagens.get(o),
																											imagens.get(p),
																											imagens.get(q),
																											imagens.get(r),
																											imagens.get(s))) / 2);
																					for (int t = s + 1; t < imagens
																							.size(); t++) {
																						finalRes.put(imagens.get(a)
																								+ " " + imagens.get(b)
																								+ " " + imagens.get(c)
																								+ " " + imagens.get(d)
																								+ " " + imagens.get(e)
																								+ " " + imagens.get(f)
																								+ " " + imagens.get(g)
																								+ " " + imagens.get(h)
																								+ " " + imagens.get(i)
																								+ " " + imagens.get(j)
																								+ " " + imagens.get(k)
																								+ " " + imagens.get(l)
																								+ " " + imagens.get(m)
																								+ " " + imagens.get(n)
																								+ " " + imagens.get(o)
																								+ " " + imagens.get(p)
																								+ " " + imagens.get(q)
																								+ " " + imagens.get(r)
																								+ " " + imagens.get(s)
																								+ " " + imagens.get(t),
																								(intersect(true,
																										imagens.get(a),
																										imagens.get(b),
																										imagens.get(c),
																										imagens.get(d),
																										imagens.get(e),
																										imagens.get(f),
																										imagens.get(g),
																										imagens.get(h),
																										imagens.get(i),
																										imagens.get(j),
																										imagens.get(k),
																										imagens.get(l),
																										imagens.get(m),
																										imagens.get(n),
																										imagens.get(o),
																										imagens.get(p),
																										imagens.get(q),
																										imagens.get(r),
																										imagens.get(s),
																										imagens.get(
																												t)) + intersect(false,
																														imagens.get(a),
																														imagens.get(b),
																														imagens.get(c),
																														imagens.get(d),
																														imagens.get(e),
																														imagens.get(f),
																														imagens.get(g),
																														imagens.get(h),
																														imagens.get(i),
																														imagens.get(j),
																														imagens.get(k),
																														imagens.get(l),
																														imagens.get(m),
																														imagens.get(n),
																														imagens.get(o),
																														imagens.get(p),
																														imagens.get(q),
																														imagens.get(r),
																														imagens.get(s),
																														imagens.get(
																																t))) / 2);
																						for (int u = t + 1; u < imagens
																								.size(); u++) {
																							finalRes.put(
																									imagens.get(a) + " "
																											+ imagens
																													.get(b)
																											+ " "
																											+ imagens
																													.get(c)
																											+ " "
																											+ imagens
																													.get(d)
																											+ " "
																											+ imagens
																													.get(e)
																											+ " "
																											+ imagens
																													.get(f)
																											+ " "
																											+ imagens
																													.get(g)
																											+ " "
																											+ imagens
																													.get(h)
																											+ " "
																											+ imagens
																													.get(i)
																											+ " "
																											+ imagens
																													.get(j)
																											+ " "
																											+ imagens
																													.get(k)
																											+ " "
																											+ imagens
																													.get(l)
																											+ " "
																											+ imagens
																													.get(m)
																											+ " "
																											+ imagens
																													.get(n)
																											+ " "
																											+ imagens
																													.get(o)
																											+ " "
																											+ imagens
																													.get(p)
																											+ " "
																											+ imagens
																													.get(q)
																											+ " "
																											+ imagens
																													.get(r)
																											+ " "
																											+ imagens
																													.get(s)
																											+ " "
																											+ imagens
																													.get(t)
																											+ " "
																											+ imagens
																													.get(u),
																									(intersect(true,
																											imagens.get(
																													a),
																											imagens.get(
																													b),
																											imagens.get(
																													c),
																											imagens.get(
																													d),
																											imagens.get(
																													e),
																											imagens.get(
																													f),
																											imagens.get(
																													g),
																											imagens.get(
																													h),
																											imagens.get(
																													i),
																											imagens.get(
																													j),
																											imagens.get(
																													k),
																											imagens.get(
																													l),
																											imagens.get(
																													m),
																											imagens.get(
																													n),
																											imagens.get(
																													o),
																											imagens.get(
																													p),
																											imagens.get(
																													q),
																											imagens.get(
																													r),
																											imagens.get(
																													s),
																											imagens.get(
																													t),
																											imagens.get(
																													u)) + intersect(false, 
																															imagens.get(
																																	a),
																															imagens.get(
																																	b),
																															imagens.get(
																																	c),
																															imagens.get(
																																	d),
																															imagens.get(
																																	e),
																															imagens.get(
																																	f),
																															imagens.get(
																																	g),
																															imagens.get(
																																	h),
																															imagens.get(
																																	i),
																															imagens.get(
																																	j),
																															imagens.get(
																																	k),
																															imagens.get(
																																	l),
																															imagens.get(
																																	m),
																															imagens.get(
																																	n),
																															imagens.get(
																																	o),
																															imagens.get(
																																	p),
																															imagens.get(
																																	q),
																															imagens.get(
																																	r),
																															imagens.get(
																																	s),
																															imagens.get(
																																	t),
																															imagens.get(
																																	u))) / 2);
																							for (int v = u
																									+ 1; v < imagens
																											.size(); v++) {
																								finalRes.put(
																										imagens.get(a)
																												+ " "
																												+ imagens
																														.get(b)
																												+ " "
																												+ imagens
																														.get(c)
																												+ " "
																												+ imagens
																														.get(d)
																												+ " "
																												+ imagens
																														.get(e)
																												+ " "
																												+ imagens
																														.get(f)
																												+ " "
																												+ imagens
																														.get(g)
																												+ " "
																												+ imagens
																														.get(h)
																												+ " "
																												+ imagens
																														.get(i)
																												+ " "
																												+ imagens
																														.get(j)
																												+ " "
																												+ imagens
																														.get(k)
																												+ " "
																												+ imagens
																														.get(l)
																												+ " "
																												+ imagens
																														.get(m)
																												+ " "
																												+ imagens
																														.get(n)
																												+ " "
																												+ imagens
																														.get(o)
																												+ " "
																												+ imagens
																														.get(p)
																												+ " "
																												+ imagens
																														.get(q)
																												+ " "
																												+ imagens
																														.get(r)
																												+ " "
																												+ imagens
																														.get(s)
																												+ " "
																												+ imagens
																														.get(t)
																												+ " "
																												+ imagens
																														.get(u)
																												+ " "
																												+ imagens
																														.get(v),
																										(intersect(true,
																												imagens.get(
																														a),
																												imagens.get(
																														b),
																												imagens.get(
																														c),
																												imagens.get(
																														d),
																												imagens.get(
																														e),
																												imagens.get(
																														f),
																												imagens.get(
																														g),
																												imagens.get(
																														h),
																												imagens.get(
																														i),
																												imagens.get(
																														j),
																												imagens.get(
																														k),
																												imagens.get(
																														l),
																												imagens.get(
																														m),
																												imagens.get(
																														n),
																												imagens.get(
																														o),
																												imagens.get(
																														p),
																												imagens.get(
																														q),
																												imagens.get(
																														r),
																												imagens.get(
																														s),
																												imagens.get(
																														t),
																												imagens.get(
																														u),
																												imagens.get(
																														v)) + intersect(false,
																																imagens.get(
																																		a),
																																imagens.get(
																																		b),
																																imagens.get(
																																		c),
																																imagens.get(
																																		d),
																																imagens.get(
																																		e),
																																imagens.get(
																																		f),
																																imagens.get(
																																		g),
																																imagens.get(
																																		h),
																																imagens.get(
																																		i),
																																imagens.get(
																																		j),
																																imagens.get(
																																		k),
																																imagens.get(
																																		l),
																																imagens.get(
																																		m),
																																imagens.get(
																																		n),
																																imagens.get(
																																		o),
																																imagens.get(
																																		p),
																																imagens.get(
																																		q),
																																imagens.get(
																																		r),
																																imagens.get(
																																		s),
																																imagens.get(
																																		t),
																																imagens.get(
																																		u),
																																imagens.get(
																																		v))) / 2);
																								for (int w = v
																										+ 1; w < imagens
																												.size(); w++) {
																									finalRes.put(
																											imagens.get(
																													a)
																													+ " "
																													+ imagens
																															.get(b)
																													+ " "
																													+ imagens
																															.get(c)
																													+ " "
																													+ imagens
																															.get(d)
																													+ " "
																													+ imagens
																															.get(e)
																													+ " "
																													+ imagens
																															.get(f)
																													+ " "
																													+ imagens
																															.get(g)
																													+ " "
																													+ imagens
																															.get(h)
																													+ " "
																													+ imagens
																															.get(i)
																													+ " "
																													+ imagens
																															.get(j)
																													+ " "
																													+ imagens
																															.get(k)
																													+ " "
																													+ imagens
																															.get(l)
																													+ " "
																													+ imagens
																															.get(m)
																													+ " "
																													+ imagens
																															.get(n)
																													+ " "
																													+ imagens
																															.get(o)
																													+ " "
																													+ imagens
																															.get(p)
																													+ " "
																													+ imagens
																															.get(q)
																													+ " "
																													+ imagens
																															.get(r)
																													+ " "
																													+ imagens
																															.get(s)
																													+ " "
																													+ imagens
																															.get(t)
																													+ " "
																													+ imagens
																															.get(u)
																													+ " "
																													+ imagens
																															.get(v)
																													+ " "
																													+ imagens
																															.get(w),
																											(intersect(true,
																													imagens.get(
																															a),
																													imagens.get(
																															b),
																													imagens.get(
																															c),
																													imagens.get(
																															d),
																													imagens.get(
																															e),
																													imagens.get(
																															f),
																													imagens.get(
																															g),
																													imagens.get(
																															h),
																													imagens.get(
																															i),
																													imagens.get(
																															j),
																													imagens.get(
																															k),
																													imagens.get(
																															l),
																													imagens.get(
																															m),
																													imagens.get(
																															n),
																													imagens.get(
																															o),
																													imagens.get(
																															p),
																													imagens.get(
																															q),
																													imagens.get(
																															r),
																													imagens.get(
																															s),
																													imagens.get(
																															t),
																													imagens.get(
																															u),
																													imagens.get(
																															v),
																													imagens.get(
																															w)) + intersect(false,
																																	imagens.get(
																																			a),
																																	imagens.get(
																																			b),
																																	imagens.get(
																																			c),
																																	imagens.get(
																																			d),
																																	imagens.get(
																																			e),
																																	imagens.get(
																																			f),
																																	imagens.get(
																																			g),
																																	imagens.get(
																																			h),
																																	imagens.get(
																																			i),
																																	imagens.get(
																																			j),
																																	imagens.get(
																																			k),
																																	imagens.get(
																																			l),
																																	imagens.get(
																																			m),
																																	imagens.get(
																																			n),
																																	imagens.get(
																																			o),
																																	imagens.get(
																																			p),
																																	imagens.get(
																																			q),
																																	imagens.get(
																																			r),
																																	imagens.get(
																																			s),
																																	imagens.get(
																																			t),
																																	imagens.get(
																																			u),
																																	imagens.get(
																																			v),
																																	imagens.get(
																																			w))) / 2);
																									for (int x = w
																											+ 1; x < imagens
																													.size(); x++) {
																										finalRes.put(
																												imagens.get(
																														a)
																														+ " "
																														+ imagens
																																.get(b)
																														+ " "
																														+ imagens
																																.get(c)
																														+ " "
																														+ imagens
																																.get(d)
																														+ " "
																														+ imagens
																																.get(e)
																														+ " "
																														+ imagens
																																.get(f)
																														+ " "
																														+ imagens
																																.get(g)
																														+ " "
																														+ imagens
																																.get(h)
																														+ " "
																														+ imagens
																																.get(i)
																														+ " "
																														+ imagens
																																.get(j)
																														+ " "
																														+ imagens
																																.get(k)
																														+ " "
																														+ imagens
																																.get(l)
																														+ " "
																														+ imagens
																																.get(m)
																														+ " "
																														+ imagens
																																.get(n)
																														+ " "
																														+ imagens
																																.get(o)
																														+ " "
																														+ imagens
																																.get(p)
																														+ " "
																														+ imagens
																																.get(q)
																														+ " "
																														+ imagens
																																.get(r)
																														+ " "
																														+ imagens
																																.get(s)
																														+ " "
																														+ imagens
																																.get(t)
																														+ " "
																														+ imagens
																																.get(u)
																														+ " "
																														+ imagens
																																.get(v)
																														+ " "
																														+ imagens
																																.get(w)
																														+ " "
																														+ imagens
																																.get(x),
																												(intersect(true,
																														imagens.get(
																																a),
																														imagens.get(
																																b),
																														imagens.get(
																																c),
																														imagens.get(
																																d),
																														imagens.get(
																																e),
																														imagens.get(
																																f),
																														imagens.get(
																																g),
																														imagens.get(
																																h),
																														imagens.get(
																																i),
																														imagens.get(
																																j),
																														imagens.get(
																																k),
																														imagens.get(
																																l),
																														imagens.get(
																																m),
																														imagens.get(
																																n),
																														imagens.get(
																																o),
																														imagens.get(
																																p),
																														imagens.get(
																																q),
																														imagens.get(
																																r),
																														imagens.get(
																																s),
																														imagens.get(
																																t),
																														imagens.get(
																																u),
																														imagens.get(
																																v),
																														imagens.get(
																																w),
																														imagens.get(
																																x)) + intersect(false,
																																		imagens.get(
																																				a),
																																		imagens.get(
																																				b),
																																		imagens.get(
																																				c),
																																		imagens.get(
																																				d),
																																		imagens.get(
																																				e),
																																		imagens.get(
																																				f),
																																		imagens.get(
																																				g),
																																		imagens.get(
																																				h),
																																		imagens.get(
																																				i),
																																		imagens.get(
																																				j),
																																		imagens.get(
																																				k),
																																		imagens.get(
																																				l),
																																		imagens.get(
																																				m),
																																		imagens.get(
																																				n),
																																		imagens.get(
																																				o),
																																		imagens.get(
																																				p),
																																		imagens.get(
																																				q),
																																		imagens.get(
																																				r),
																																		imagens.get(
																																				s),
																																		imagens.get(
																																				t),
																																		imagens.get(
																																				u),
																																		imagens.get(
																																				v),
																																		imagens.get(
																																				w),
																																		imagens.get(
																																				x))) / 2);
																										for (int y = x
																												+ 1; y < imagens
																														.size(); y++) {
																											finalRes.put(
																													imagens.get(
																															a)
																															+ " "
																															+ imagens
																																	.get(b)
																															+ " "
																															+ imagens
																																	.get(c)
																															+ " "
																															+ imagens
																																	.get(d)
																															+ " "
																															+ imagens
																																	.get(e)
																															+ " "
																															+ imagens
																																	.get(f)
																															+ " "
																															+ imagens
																																	.get(g)
																															+ " "
																															+ imagens
																																	.get(h)
																															+ " "
																															+ imagens
																																	.get(i)
																															+ " "
																															+ imagens
																																	.get(j)
																															+ " "
																															+ imagens
																																	.get(k)
																															+ " "
																															+ imagens
																																	.get(l)
																															+ " "
																															+ imagens
																																	.get(m)
																															+ " "
																															+ imagens
																																	.get(n)
																															+ " "
																															+ imagens
																																	.get(o)
																															+ " "
																															+ imagens
																																	.get(p)
																															+ " "
																															+ imagens
																																	.get(q)
																															+ " "
																															+ imagens
																																	.get(r)
																															+ " "
																															+ imagens
																																	.get(s)
																															+ " "
																															+ imagens
																																	.get(t)
																															+ " "
																															+ imagens
																																	.get(u)
																															+ " "
																															+ imagens
																																	.get(v)
																															+ " "
																															+ imagens
																																	.get(w)
																															+ " "
																															+ imagens
																																	.get(x)
																															+ " "
																															+ imagens
																																	.get(y),
																													(intersect(true,
																															imagens.get(
																																	a),
																															imagens.get(
																																	b),
																															imagens.get(
																																	c),
																															imagens.get(
																																	d),
																															imagens.get(
																																	e),
																															imagens.get(
																																	f),
																															imagens.get(
																																	g),
																															imagens.get(
																																	h),
																															imagens.get(
																																	i),
																															imagens.get(
																																	j),
																															imagens.get(
																																	k),
																															imagens.get(
																																	l),
																															imagens.get(
																																	m),
																															imagens.get(
																																	n),
																															imagens.get(
																																	o),
																															imagens.get(
																																	p),
																															imagens.get(
																																	q),
																															imagens.get(
																																	r),
																															imagens.get(
																																	s),
																															imagens.get(
																																	t),
																															imagens.get(
																																	u),
																															imagens.get(
																																	v),
																															imagens.get(
																																	w),
																															imagens.get(
																																	x),
																															imagens.get(
																																	y)) + intersect(false,
																																			imagens.get(
																																					a),
																																			imagens.get(
																																					b),
																																			imagens.get(
																																					c),
																																			imagens.get(
																																					d),
																																			imagens.get(
																																					e),
																																			imagens.get(
																																					f),
																																			imagens.get(
																																					g),
																																			imagens.get(
																																					h),
																																			imagens.get(
																																					i),
																																			imagens.get(
																																					j),
																																			imagens.get(
																																					k),
																																			imagens.get(
																																					l),
																																			imagens.get(
																																					m),
																																			imagens.get(
																																					n),
																																			imagens.get(
																																					o),
																																			imagens.get(
																																					p),
																																			imagens.get(
																																					q),
																																			imagens.get(
																																					r),
																																			imagens.get(
																																					s),
																																			imagens.get(
																																					t),
																																			imagens.get(
																																					u),
																																			imagens.get(
																																					v),
																																			imagens.get(
																																					w),
																																			imagens.get(
																																					x),
																																			imagens.get(
																																					y))) / 2);
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File("final_result.txt")));
		for (String i : finalRes.keySet()) {
			writer.write(i);
			writer.write(":");
			writer.write(String.valueOf(finalRes.get(i)));
			writer.write("\n");
		}
		writer.flush();
		writer.close();
	}

	private static Integer min(String... images) {
		int min = Integer.MAX_VALUE;
		for (int i = 0; i < images.length; i++) {
			for (int j = i + 1; j < images.length; j++) {
				int intersect;
				if (intersection.get(images[i] + " " + images[j]) != null) {
					intersect = intersection.get(images[i] + " " + images[j]);
				} else {
					intersect = intersection.get(images[j] + " " + images[i]);
				}
				if (intersect < min) {
					min = intersect;
				}
			}
		}
		return min;
	}

	private static Integer intersect(boolean empty, String... images) {
		Integer result = 0;
		for (String i : images) {
			result += individual.get(i);
		}

		for (int a = 0; a < images.length; a++) {
			for (int b = a + 1; b < images.length; b++) {
				result -= min(images[a], images[b]);
			}
		}
		if (!empty) {
			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						result += min(images[a], images[b], images[c]);
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							result -= min(images[a], images[b], images[c], images[d]);
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								result += min(images[a], images[b], images[c], images[d], images[e]);
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									result -= min(images[a], images[b], images[c], images[d], images[e], images[f]);
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										result += min(images[a], images[b], images[c], images[d], images[e], images[f],
												images[g]);
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											result -= min(images[a], images[b], images[c], images[d], images[e],
													images[f], images[g], images[h]);
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												result += min(images[a], images[b], images[c], images[d], images[e],
														images[f], images[g], images[h], images[i]);
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													result -= min(images[a], images[b], images[c], images[d], images[e],
															images[f], images[g], images[h], images[i], images[j]);
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														result += min(images[a], images[b], images[c], images[d],
																images[e], images[f], images[g], images[h], images[i],
																images[j], images[k]);
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															result -= min(images[a], images[b], images[c], images[d],
																	images[e], images[f], images[g], images[h],
																	images[i], images[j], images[k], images[l]);
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																result += min(images[a], images[b], images[c],
																		images[d], images[e], images[f], images[g],
																		images[h], images[i], images[j], images[k],
																		images[l], images[m]);
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	result -= min(images[a], images[b], images[c],
																			images[d], images[e], images[f], images[g],
																			images[h], images[i], images[j], images[k],
																			images[l], images[m], images[n]);
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		result += min(images[a], images[b], images[c],
																				images[d], images[e], images[f],
																				images[g], images[h], images[i],
																				images[j], images[k], images[l],
																				images[m], images[n], images[o]);
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			result -= min(images[a], images[b],
																					images[c], images[d], images[e],
																					images[f], images[g], images[h],
																					images[i], images[j], images[k],
																					images[l], images[m], images[n],
																					images[o], images[p]);
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				result += min(images[a], images[b],
																						images[c], images[d], images[e],
																						images[f], images[g], images[h],
																						images[i], images[j], images[k],
																						images[l], images[m], images[n],
																						images[o], images[p],
																						images[q]);
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					result -= min(images[a], images[b],
																							images[c], images[d],
																							images[e], images[f],
																							images[g], images[h],
																							images[i], images[j],
																							images[k], images[l],
																							images[m], images[n],
																							images[o], images[p],
																							images[q], images[r]);
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						result += min(images[a],
																								images[b], images[c],
																								images[d], images[e],
																								images[f], images[g],
																								images[h], images[i],
																								images[j], images[k],
																								images[l], images[m],
																								images[n], images[o],
																								images[p], images[q],
																								images[r], images[s]);
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						for (int t = s
																								+ 1; t < images.length; t++) {
																							result -= min(images[a],
																									images[b],
																									images[c],
																									images[d],
																									images[e],
																									images[f],
																									images[g],
																									images[h],
																									images[i],
																									images[j],
																									images[k],
																									images[l],
																									images[m],
																									images[n],
																									images[o],
																									images[p],
																									images[q],
																									images[r],
																									images[s],
																									images[t]);
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						for (int t = s
																								+ 1; t < images.length; t++) {
																							for (int u = t
																									+ 1; u < images.length; u++) {
																								result += min(images[a],
																										images[b],
																										images[c],
																										images[d],
																										images[e],
																										images[f],
																										images[g],
																										images[h],
																										images[i],
																										images[j],
																										images[k],
																										images[l],
																										images[m],
																										images[n],
																										images[o],
																										images[p],
																										images[q],
																										images[r],
																										images[s],
																										images[t],
																										images[u]);
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						for (int t = s
																								+ 1; t < images.length; t++) {
																							for (int u = t
																									+ 1; u < images.length; u++) {
																								for (int v = u
																										+ 1; v < images.length; v++) {
																									result -= min(
																											images[a],
																											images[b],
																											images[c],
																											images[d],
																											images[e],
																											images[f],
																											images[g],
																											images[h],
																											images[i],
																											images[j],
																											images[k],
																											images[l],
																											images[m],
																											images[n],
																											images[o],
																											images[p],
																											images[q],
																											images[r],
																											images[s],
																											images[t],
																											images[u],
																											images[v]);
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						for (int t = s
																								+ 1; t < images.length; t++) {
																							for (int u = t
																									+ 1; u < images.length; u++) {
																								for (int v = u
																										+ 1; v < images.length; v++) {
																									for (int x = v
																											+ 1; x < images.length; x++) {
																										result += min(
																												images[a],
																												images[b],
																												images[c],
																												images[d],
																												images[e],
																												images[f],
																												images[g],
																												images[h],
																												images[i],
																												images[j],
																												images[k],
																												images[l],
																												images[m],
																												images[n],
																												images[o],
																												images[p],
																												images[q],
																												images[r],
																												images[s],
																												images[t],
																												images[u],
																												images[v],
																												images[x]);
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						for (int t = s
																								+ 1; t < images.length; t++) {
																							for (int u = t
																									+ 1; u < images.length; u++) {
																								for (int v = u
																										+ 1; v < images.length; v++) {
																									for (int x = v
																											+ 1; x < images.length; x++) {
																										for (int w = x
																												+ 1; w < images.length; w++) {
																											result -= min(
																													images[a],
																													images[b],
																													images[c],
																													images[d],
																													images[e],
																													images[f],
																													images[g],
																													images[h],
																													images[i],
																													images[j],
																													images[k],
																													images[l],
																													images[m],
																													images[n],
																													images[o],
																													images[p],
																													images[q],
																													images[r],
																													images[s],
																													images[t],
																													images[u],
																													images[v],
																													images[x],
																													images[w]);
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			for (int a = 0; a < images.length; a++) {
				for (int b = a + 1; b < images.length; b++) {
					for (int c = b + 1; c < images.length; c++) {
						for (int d = c + 1; d < images.length; d++) {
							for (int e = d + 1; e < images.length; e++) {
								for (int f = e + 1; f < images.length; f++) {
									for (int g = f + 1; g < images.length; g++) {
										for (int h = g + 1; h < images.length; h++) {
											for (int i = h + 1; i < images.length; i++) {
												for (int j = i + 1; j < images.length; j++) {
													for (int k = j + 1; k < images.length; k++) {
														for (int l = k + 1; l < images.length; l++) {
															for (int m = l + 1; m < images.length; m++) {
																for (int n = m + 1; n < images.length; n++) {
																	for (int o = n + 1; o < images.length; o++) {
																		for (int p = o + 1; p < images.length; p++) {
																			for (int q = p
																					+ 1; q < images.length; q++) {
																				for (int r = q
																						+ 1; r < images.length; r++) {
																					for (int s = r
																							+ 1; s < images.length; s++) {
																						for (int t = s
																								+ 1; t < images.length; t++) {
																							for (int u = t
																									+ 1; u < images.length; u++) {
																								for (int v = u
																										+ 1; v < images.length; v++) {
																									for (int x = v
																											+ 1; x < images.length; x++) {
																										for (int w = x
																												+ 1; w < images.length; w++) {
																											for (int y = w
																													+ 1; y < images.length; y++) {
																												result += min(
																														images[a],
																														images[b],
																														images[c],
																														images[d],
																														images[e],
																														images[f],
																														images[g],
																														images[h],
																														images[i],
																														images[j],
																														images[k],
																														images[l],
																														images[m],
																														images[n],
																														images[o],
																														images[p],
																														images[q],
																														images[r],
																														images[s],
																														images[t],
																														images[u],
																														images[v],
																														images[x],
																														images[w],
																														images[y]);
																											}
																										}
																									}
																								}
																							}
																						}
																					}
																				}
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
		}

		return result;
	}

}
