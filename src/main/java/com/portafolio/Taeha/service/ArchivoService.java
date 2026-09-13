package com.portafolio.Taeha.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Service
public class ArchivoService {

    private final Path carpetaImagenes;
    private final Path carpetaArchivos;

    public ArchivoService(
            @Value("${app.upload.dir:uploads}")
            String directorio) {

        this.carpetaImagenes =
                Paths.get(directorio, "imagenes")
                        .toAbsolutePath()
                        .normalize();

        this.carpetaArchivos =
                Paths.get(directorio, "archivos")
                        .toAbsolutePath()
                        .normalize();

        try {

            Files.createDirectories(carpetaImagenes);
            Files.createDirectories(carpetaArchivos);

        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudieron crear las carpetas de archivos",
                    e
            );
        }
    }


    public String guardarImagen(MultipartFile archivo) {

        return guardarArchivo(
                archivo,
                carpetaImagenes
        );
    }


    public String guardarArchivo(MultipartFile archivo) {

        return guardarArchivo(
                archivo,
                carpetaArchivos
        );
    }


    private String guardarArchivo(
            MultipartFile archivo,
            Path carpeta) {

        if (archivo == null || archivo.isEmpty()) {

            return null;
        }

        try {

            String nombreOriginal =
                    archivo.getOriginalFilename();

            if (nombreOriginal == null
                    || nombreOriginal.isBlank()) {

                throw new RuntimeException(
                        "El archivo no tiene nombre"
                );
            }


            String extension = "";

            int punto =
                    nombreOriginal.lastIndexOf(".");


            if (punto >= 0) {

                extension =
                        nombreOriginal
                                .substring(punto)
                                .toLowerCase();
            }


            String nombreNuevo =
                    UUID.randomUUID() + extension;


            Path destino =
                    carpeta
                            .resolve(nombreNuevo)
                            .normalize();


            if (!destino.startsWith(carpeta)) {

                throw new RuntimeException(
                        "Ruta de archivo no válida"
                );
            }


            Files.copy(
                    archivo.getInputStream(),
                    destino,
                    StandardCopyOption.REPLACE_EXISTING
            );


            return nombreNuevo;


        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al guardar el archivo",
                    e
            );
        }
    }


    public Resource cargarImagen(String nombre) {

        return cargar(
                carpetaImagenes,
                nombre
        );
    }


    public Resource cargarArchivo(String nombre) {

        return cargar(
                carpetaArchivos,
                nombre
        );
    }


    private Resource cargar(
            Path carpeta,
            String nombre) {

        try {

            Path archivo =
                    carpeta
                            .resolve(nombre)
                            .normalize();


            if (!archivo.startsWith(carpeta)) {

                return null;
            }


            Resource resource =
                    new UrlResource(
                            archivo.toUri()
                    );


            if (resource.exists()
                    && resource.isReadable()) {

                return resource;
            }


            return null;


        } catch (MalformedURLException e) {

            return null;
        }
    }


    public void eliminarImagen(String nombre) {

        eliminar(
                carpetaImagenes,
                nombre
        );
    }


    public void eliminarArchivo(String nombre) {

        eliminar(
                carpetaArchivos,
                nombre
        );
    }


    private void eliminar(
            Path carpeta,
            String nombre) {

        if (nombre == null
                || nombre.isBlank()) {

            return;
        }


        try {

            Path archivo =
                    carpeta
                            .resolve(nombre)
                            .normalize();


            if (!archivo.startsWith(carpeta)) {

                return;
            }


            Files.deleteIfExists(archivo);


        } catch (IOException e) {

            throw new RuntimeException(
                    "No se pudo eliminar el archivo: "
                            + nombre,
                    e
            );
        }
    }
}