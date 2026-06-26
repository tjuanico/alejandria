package es.utamed.alejandria.web;

import es.utamed.alejandria.ejb.services.ConfigurationService;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@WebServlet("/archivos/*")
public class PdfServlet extends HttpServlet {

    @Inject
    private ConfigurationService configService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String nombreFichero = request.getPathInfo().substring(1);
        File archivo = new File(configService.getValor("pathPdf"), nombreFichero);

        if (!archivo.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "El documento no existe en el disco.");
            return;
        }

        // Configuramos las cabeceras para que el navegador lo muestre embebido
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=\"" + archivo.getName() + "\"");
        response.setContentLengthLong(archivo.length());

        // Copiamos el archivo del disco a la respuesta HTTP
        Files.copy(archivo.toPath(), response.getOutputStream());
    }
}
