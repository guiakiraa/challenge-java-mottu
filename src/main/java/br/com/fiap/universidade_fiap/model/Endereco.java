package br.com.fiap.universidade_fiap.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "endereco")
public class Endereco {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    @NotBlank(message = "Logradouro é obrigatório")
    @Size(max = 255, message = "Logradouro deve ter até 255 caracteres")
    private String logradouro;

    @Column(nullable = false)
    @NotNull(message = "Número é obrigatório")
    @Min(value = 1, message = "Número deve ser positivo")
    private Integer numero;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Bairro é obrigatório")
    @Size(max = 100, message = "Bairro deve ter até 100 caracteres")
    private String bairro;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Cidade é obrigatória")
    @Size(max = 100, message = "Cidade deve ter até 100 caracteres")
    private String cidade;

    @Column(nullable = false, length = 2)
    @NotBlank(message = "Estado é obrigatório")
    @Pattern(regexp = "[A-Z]{2}", message = "Estado deve ser UF com 2 letras maiúsculas")
    private String estado;

    @Column(nullable = false, length = 9)
    @NotBlank(message = "CEP é obrigatório")
    @Pattern(regexp = "\\d{5}-?\\d{3}", message = "CEP deve estar no formato 00000-000")
    private String cep;

    @Column(length = 255)
    @Size(max = 255, message = "Complemento deve ter até 255 caracteres")
    private String complemento;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }
}


