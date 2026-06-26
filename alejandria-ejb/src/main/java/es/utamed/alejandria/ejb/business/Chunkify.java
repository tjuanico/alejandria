package es.utamed.alejandria.ejb.business;

import es.utamed.alejandria.ejb.utils.VentanaSolapamiento;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
   TFM, UTAMED
   Propósito: Dividir en chunks con solapamiento un string de un pdf
   @since: 18/03/2026
   @author: Antoni Juanico
 */
public class Chunkify {

    private static final int CHUNK_MIN_SIZE = 5000;
    private static final int LINEAS_VENTANA_SOLAPMIENTO = 3;

    // Métodos privados
    private List<String> chunks;

    // Getters & Setters
    public List<String> getChunks() { return this.chunks; }

    // Constructor
    public Chunkify(PDDocument docPDF) throws IOException {
        // A partir del string del PDF vamos a obtener la lista de chunks, trozos del pdf solapados
        // para vectorizar y almacenar en la base de datos

        this.chunks = new ArrayList<>();
        String[] lineas = obtenerLineasDocumento(docPDF);

        StringBuilder chunkActual = new StringBuilder();
        VentanaSolapamiento ventanaSolapamiento = new VentanaSolapamiento(LINEAS_VENTANA_SOLAPMIENTO);

        int lengthActual = 0;
        int lineaInicialChunk = 0;

        for (int i = 0; i < lineas.length; i++) {
            String lineaLimpia = lineas[i].trim();
            if (lineaLimpia.isEmpty()) continue;

            if (!chunkActual.isEmpty() && lengthActual >= CHUNK_MIN_SIZE) { // Miramos si desbordamos

                String chunkResultante = ventanaSolapamiento.getLastWindow() + " " + chunkActual.toString().trim();
                this.chunks.add(formatearChunk(chunkResultante,lineaInicialChunk - ventanaSolapamiento.getLastNumberLines(), i-1));
                ventanaSolapamiento.swapWindow();
                chunkActual.setLength(0);
                lineaInicialChunk = i;
                lengthActual = 0;
            }

            chunkActual.append(lineaLimpia).append(" "); // Añadimos la línea al chunkActual
            lengthActual +=  lineaLimpia.length() + 1;
            ventanaSolapamiento.add(lineaLimpia); // Añadimos la línea a la ventana de solapamiento

        }

        if (!chunkActual.isEmpty()) {
            String chunkFinal = (ventanaSolapamiento.getLastWindow() + " " + chunkActual.toString()).trim();
            this.chunks.add(formatearChunk(chunkFinal, lineaInicialChunk - ventanaSolapamiento.getLastNumberLines(), lineas.length - 1));
        }
    }

    private String[] obtenerLineasDocumento(PDDocument doc) throws IOException {
        PDFTextStripper stripper = new PDFTextStripper();

        stripper.setSortByPosition(true); // Maintains physical layout
        stripper.setStartPage(1); // First page to extract
        stripper.setEndPage(doc.getNumberOfPages()); // Last page

        String strPDF = stripper.getText(doc);

        return strPDF.split(stripper.getLineSeparator());
    }

    private String formatearChunk(String chunkResultante, int lineaInicial, int lineaFinal) {
        String textoLimpio = chunkResultante
                .replace("\\", "\\\\")
                .replace("\"", "\\\"")
                .replace("\n", " ")
                .replace("\r", "")
                .replaceAll("\\s+"," ") // elimina múltiples espacios
                .trim();

        return String.format("{\"content\": \"%s\", \"start_line\": %d, \"end_line\": %d}", textoLimpio, lineaInicial, lineaFinal);
    }
}
