package br.com.leaf.class3.dynamo.model;

public class UsuarioResponse {

    private final String id;
    private final String nome;

    public UsuarioResponse(String id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public String getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}
