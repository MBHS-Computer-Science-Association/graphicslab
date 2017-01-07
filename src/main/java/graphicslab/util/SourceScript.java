package graphicslab.util;
import java.io.BufferedReader;
import java.io.FileReader;

public class SourceScript {
    public static void main(String[] args) throws Exception {
        BufferedReader input = new BufferedReader(new FileReader("src/main/res/fragmentskyboxshader.fs"));
        StringBuilder output = new StringBuilder();
        
        String line;
        
        line = input.readLine(); // #version
        output.append("    protected static CharSequence source = \"\"" + '\n');
        output.append("            + \"" + line + "\\n\"" + '\n');
        
        while ((line = input.readLine()) != null) {
            if (line.contains("//")) {
                output.append("               " + line + '\n');
            } else if (line.matches("\\s*")) {
                output.append('\n');
            } else if (input.ready()) {                
                output.append("            + \"" + line + "\"" + '\n');
            } else {
                output.append("            + \"" + line + "\"" +';' + '\n');
            }
            
        }
        
        System.out.print(output);
    }
}
