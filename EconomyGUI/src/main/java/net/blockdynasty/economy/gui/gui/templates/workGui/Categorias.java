package net.blockdynasty.economy.gui.gui.templates.workGui;

import java.util.HashMap;

public class Categorias {
    private static final HashMap<String,String> categorias;

    static {
        categorias = new HashMap<>();

        categorias.put("AFK","http://textures.minecraft.net/texture/5703ec7638791df4ac2f208942d0fd737f163d355c783b452f26652fd1878062");
        categorias.put("Decorador","http://textures.minecraft.net/texture/465d225e3ab682fccabdf7cce7dc4538280150375506f836698f6e0f800b5c38");
        categorias.put("Recolector","http://textures.minecraft.net/texture/3683465f7d0da2dd054d04eb974d7db92904781177fd080385b6bc8ac0127b09");
        categorias.put("Constructor","http://textures.minecraft.net/texture/a5bae982f483547d35afb16986a4b1d811c54da1bbfa5834362dc0af01f5692c");
        categorias.put("Luchador","http://textures.minecraft.net/texture/b01cbfb414760efe504d4af739708a18b89b6145d7659bcf526f1e42d7bedb37");
        categorias.put("Explorador","http://textures.minecraft.net/texture/e6315e380545b99ee9c8d1b217cde2d8884ae873e00744e0d05d7663f41882cf");
        categorias.put("Granjero","http://textures.minecraft.net/texture/33235c98db07f976def27ceaaca89c7717831b4bcc7f2f3a14622e78c2dbe");
        categorias.put("Cazador","http://textures.minecraft.net/texture/50dfc8a3563bf996f5c1b74b0b015b2cceb2d04f94bbcdafb2299d8a5979fac1");
        categorias.put("Talador","http://textures.minecraft.net/texture/64ba49384dba7b7acdb4f70e9361e6d57cbbcbf720cf4f16c2bb83e4557");
        categorias.put("Minero","http://textures.minecraft.net/texture/b6ea2135838461534372f2da6c862d21cd5f3d2c7119f2bb674bbd42791");
        categorias.put("Aplanador","http://textures.minecraft.net/texture/18d7d19a3fd8e26524aab32f944dc7d2f560e1d8fe178e893ceea74cfcf");
    }

    public static String getTexture(String categoria){
        return categorias.getOrDefault(categoria,"Unknown");
    }
}
