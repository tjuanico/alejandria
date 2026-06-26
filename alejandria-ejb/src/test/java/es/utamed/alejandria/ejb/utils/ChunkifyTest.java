package es.utamed.alejandria.ejb.utils;

import es.utamed.alejandria.ejb.business.Chunkify;
import es.utamed.alejandria.ejb.business.Embedding;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class ChunkifyTest {

    @Test
    public void test_chunkify() {
        File myFile = new File("c:/temp/prueba.pdf");

        PDDocument doc = null;
        try {
            doc = Loader.loadPDF(myFile);
            Chunkify chunkify = new Chunkify(doc);

            for (String chunk : chunkify.getChunks()) {
                System.out.println(chunk);
                /*Embedding embedding = new Embedding();
                float[] resultEmeddings = embedding.generateEmbedding(chunk);
                System.out.println(Arrays.toString(resultEmeddings));*/
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
