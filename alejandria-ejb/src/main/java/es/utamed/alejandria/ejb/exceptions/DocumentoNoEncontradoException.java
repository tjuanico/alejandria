package es.utamed.alejandria.ejb.exceptions;

public class DocumentoNoEncontradoException extends RuntimeException {
    public DocumentoNoEncontradoException(String message) {
        super(message);
    }
}
