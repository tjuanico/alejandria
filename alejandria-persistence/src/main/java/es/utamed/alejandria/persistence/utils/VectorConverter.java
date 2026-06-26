package es.utamed.alejandria.persistence.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/*
   TFM, UTAMED
   Propósito: Conversor JPA altamente optimizado para transformar arrays de
              float a formato binario nativo (Little-Endian) para MariaDB VECTOR.
   @since: 14/03/2026
   @author: Antoni Juanico
 */

@Converter(autoApply = false)
public class VectorConverter implements AttributeConverter<float[], byte[]> {

    @Override
    public byte[] convertToDatabaseColumn(float[] vectorArray) {
        if (vectorArray == null || vectorArray.length == 0) {
            return null;
        }

        // MariaDB requiere formato Little-Endian para los vectores binarios
        ByteBuffer buffer = ByteBuffer.allocate(vectorArray.length * 4).order(ByteOrder.LITTLE_ENDIAN);

        for (float v : vectorArray) {
            buffer.putFloat(v);
        }

        return buffer.array();
    }

    @Override
    public float[] convertToEntityAttribute(byte[] dbData) {
        if (dbData == null || dbData.length == 0) {
            return new float[0];
        }

        // Transformamos los bytes binarios de MariaDB de vuelta a floats de Java
        FloatBuffer floatBuffer = ByteBuffer.wrap(dbData).order(ByteOrder.LITTLE_ENDIAN).asFloatBuffer();
        float[] floatArray = new float[floatBuffer.remaining()];
        floatBuffer.get(floatArray);

        return floatArray;
    }
}