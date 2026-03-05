package com.es2.composite;

public class Main {

    public static void main(String[] args) {
        SubMenu rootMenu = new SubMenu();
        rootMenu.setLabel("Menu Principal");

        Link home = new Link();
        home.setLabel("Home");
        home.setURL("https://example.com/home");

        Link contact = new Link();
        contact.setLabel("Contato");
        contact.setURL("https://example.com/contato");

        SubMenu docsMenu = new SubMenu();
        docsMenu.setLabel("Documentação");

        Link api = new Link();
        api.setLabel("API");
        api.setURL("https://example.com/docs/api");

        Link guide = new Link();
        guide.setLabel("Guia");
        guide.setURL("https://example.com/docs/guia");

        docsMenu.addChild(api);
        docsMenu.addChild(guide);

        rootMenu.addChild(home);
        rootMenu.addChild(contact);
        rootMenu.addChild(docsMenu);

        rootMenu.showOptions();
    }
}
