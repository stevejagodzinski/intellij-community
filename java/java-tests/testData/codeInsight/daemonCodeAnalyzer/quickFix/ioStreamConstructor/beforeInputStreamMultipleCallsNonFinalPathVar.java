// "Fix all '`InputStream' and 'OutputStream' can be constructed using 'Files' methods' problems in file" "true"
import java.io.*;
import java.nio.*;

class Foo {
  void test(File file, String str) {
    Path filePath = file.toPath();
    try (InputStream is = Files.newInputStream(filePath)) {
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    
    filePath = Path.of("other");

    try (InputStream is = new FileInputStream(file)) {
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    Path strPath = Path.of(str);
    for (int i = 0; i < 10; i++) {
      try (InputStream is = new FileInp<caret>utStream(str)){
      } catch(IOException e){
        throw new RuntimeException(e);
      }

      strPath = Path.of("foo");
      try (InputStream is = Files.newInputStream(strPath)) {
      }
      catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}