package es.utamed.alejandria.ejb.utils;


import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

/*
   TFM, UTAMED
   Propósito: Guardar en disco el fichero pdf a partir del InputStream
   @since: 18/03/2026
   @author: Antoni Juanico
 */
public class PersistFile {

    private Path path;
    private String fileName;

    public PersistFile(String ruta, String fileName) {
        this.path = Paths.get(ruta);
        this.fileName = fileName;
    }

    public void save(InputStream fileContent) throws IOException {
        Path rutaFinal = this.path.resolve(this.fileName);
        Files.copy(fileContent, rutaFinal, StandardCopyOption.REPLACE_EXISTING);

    }

}
