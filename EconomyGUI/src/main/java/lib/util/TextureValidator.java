package lib.util;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Base64;

public class TextureValidator {
    private static final String HTTP_TEXTURE_PREFIX = "http://textures.minecraft.net/texture/";
    private static final String HTTPS_TEXTURE_PREFIX = "https://textures.minecraft.net/texture/";

    public static String validateInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }

        if (input.equalsIgnoreCase("clear")) {
            return "";
        }

        String trimmedInput = input.trim();

        if (isMinecraftTextureUrl(trimmedInput)) {
            return validateMinecraftUrl(trimmedInput) ? trimmedInput : null;
        }

        String urlFromBase64 = extractUrlFromBase64(trimmedInput);
        if (urlFromBase64 != null) {
            return validateMinecraftUrl(urlFromBase64) ? urlFromBase64 : null;
        }

        String constructedUrl = HTTP_TEXTURE_PREFIX + trimmedInput;
        return validateMinecraftUrl(constructedUrl) ? constructedUrl : null;
    }

    private static boolean isMinecraftTextureUrl(String url) {
        return url != null && (url.startsWith(HTTP_TEXTURE_PREFIX) || url.startsWith(HTTPS_TEXTURE_PREFIX));
    }

    private static boolean validateMinecraftUrl(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(3000);
            connection.setReadTimeout(3000);

            int responseCode = connection.getResponseCode();
            connection.disconnect();

            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }

    private static String extractUrlFromBase64(String base64Input) {
        try {
            byte[] decodedBytes = Base64.getDecoder().decode(base64Input);
            String decodedString = new String(decodedBytes);

            JsonObject json = JsonParser.parseString(decodedString).getAsJsonObject();
            return json.getAsJsonObject("textures")
                    .getAsJsonObject("SKIN")
                    .get("url")
                    .getAsString();

        } catch (Exception e) {
            return null;
        }
    }
}