package com.portafolio.Taeha.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Map;

@Service
public class ArchivoService {

    @Autowired
    private Cloudinary cloudinary;

    // --- MÉTODOS DE GUARDADO EN CLOUDINARY ---

    public String guardarImagen(MultipartFile archivo) {
        return subirACloudinary(archivo, "subidas/imagenes");
    }

    public String guardarArchivo(MultipartFile archivo) {
        return subirACloudinary(archivo, "subidas/archivos");
    }

    private String subirACloudinary(MultipartFile archivo, String carpeta) {
        if (archivo == null || archivo.isEmpty()) {
            return null;
        }

try {
        Map uploadResult = cloudinary.uploader().upload(
            archivo.getBytes(),
            ObjectUtils.asMap(
                "folder", carpeta,
                "resource_type", "raw"
            )
        );

        return uploadResult.get("secure_url").toString();

    } catch (IOException e) {
        throw new RuntimeException("Error al subir archivo a Cloudinary", e);
    }
    }

    // --- MÉTODOS DE CARGA / LECTURA ---

    public Resource cargarImagen(String rutaOUrl) {
        return obtenerRecursoDesdeUrl(rutaOUrl);
    }

    public Resource cargarArchivo(String rutaOUrl) {
        return obtenerRecursoDesdeUrl(rutaOUrl);
    }

    private Resource obtenerRecursoDesdeUrl(String url) {
        if (url == null || url.isBlank()) {
            return null;
        }

        try {
            Resource resource = new UrlResource(url);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            }
            return null;
        } catch (MalformedURLException e) {
            return null;
        }
    }

    // --- MÉTODOS DE ELIMINACIÓN ---

    public void eliminarImagen(String url) {
        // Al trabajar con Cloudinary, las URLs persisten en la nube
    }

    public void eliminarArchivo(String url) {
        // Al trabajar con Cloudinary, las URLs persisten en la nube
    }
}
