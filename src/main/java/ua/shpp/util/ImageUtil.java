package ua.shpp.util;

import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.util.unit.DataSize;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Base64;

@Slf4j
public class ImageUtil {
    public static byte[] resizeImage(MultipartFile img, int targetWidth, int targetHeight) {
        BufferedImage avatar = multipartToBufferedImage(img);
        String imgFormat = getImageFormat(img);

        log.info("Resizing image width: {}, height: {}", avatar.getWidth(), avatar.getHeight());
        BufferedImage resizedAvatar = Scalr.resize(avatar, Scalr.Mode.FIT_EXACT, targetWidth, targetHeight);
        log.info("Resizing image width: {}, height: {}", resizedAvatar.getWidth(), resizedAvatar.getHeight());

        return bufferedImageToBytes(resizedAvatar, imgFormat);
    }

    public static String convertImageToBase64(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }

    private static byte[] bufferedImageToBytes(BufferedImage bufferedImage, String imgFormat) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

            ImageIO.write(bufferedImage, imgFormat, baos);
            return baos.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedImage multipartToBufferedImage(MultipartFile img) {
        try {
            InputStream inputStream = img.getInputStream();

            return ImageIO.read(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static String getImageFormat(MultipartFile img) {
        log.info("Image name: {}. Image type: {}", img.getOriginalFilename(), img.getContentType());
        String contentType = img.getContentType();

        if (contentType != null) {
            if (contentType.contains("jpeg")) {
                return "JPEG";
            } else if (contentType.contains("png")) {
                return "PNG";
            } else if (contentType.contains("heic")) {
                return "HEIC";
            } else if (contentType.contains("webp")) {
                return "WEBP";
            } else {
                throw new RuntimeException("Unsupported image format: " + contentType);
            }
        }
        throw new RuntimeException("Unable to determine image format");
    }

    public static void checkMaxImgSizeInMB(MultipartFile img, int maxImgSizeInMB) {
        int kilobyte = 1024;
        long imgSize = img.getSize();
        double imageSizeInMB = (double) imgSize / (kilobyte * kilobyte);
        log.info("Image size: {} MB", String.format("%.2f", imageSizeInMB));

        DataSize maxAvatarSize = DataSize.ofMegabytes(maxImgSizeInMB);
        if (imgSize > maxAvatarSize.toBytes()) {
            throw new RuntimeException("Image size exceeds the maximum allowed size of " + maxImgSizeInMB + " MB");
        }
    }
}