package extraction;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import com.google.common.io.Files;

public class DedupResultExtraction {
	
	public static void main(String[] args) throws IOException {
		File output = new File("/home/stack/workspace/CloudSim/output.txt");
		List<String> lines = Files.readLines(output, Charset.defaultCharset());
		int pos = 0;
		while (pos < lines.size()) {
			if (lines.get(pos).startsWith("***")) {
				pos++;
			} else {
				
				pos += 2;
			}
		}
	}

}
