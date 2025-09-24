package com.company.inventory.inventario.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class Util {
    // compress the image bytes before storing it in the database
    public static byte[] compressZLib(byte[] data) {
        System.out.println("Original Image Byte Size - " + data.length);
        
        java.util.zip.Deflater deflater = new java.util.zip.Deflater();
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        
        try {
            outputStream.close();
        } catch (IOException e) {
            System.err.println("Erro ao fechar stream de compressão: " + e.getMessage());
        }
        
        byte[] compressedData = outputStream.toByteArray();
        System.out.println("Compressed Image Byte Size - " + compressedData.length);
        
        // Calculate compression ratio
        double ratio = (double) compressedData.length / data.length * 100;
        System.out.println("Compression ratio: " + String.format("%.2f", ratio) + "%");
        
        return compressedData;
    }
    
    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressZLib(byte[] data) {
        java.util.zip.Inflater inflater = new java.util.zip.Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException ioe) {
            System.err.println("Erro de IO durante descompressão: " + ioe.getMessage());
        } catch (java.util.zip.DataFormatException e) {
            System.err.println("Erro de formato durante descompressão: " + e.getMessage());
        }
        
        return outputStream.toByteArray();
    }
}
