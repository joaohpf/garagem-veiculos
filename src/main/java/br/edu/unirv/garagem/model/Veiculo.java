package br.edu.unirv.garagem.model;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
public class Veiculo {
    
    private int id;
    @NotBlank(message = "Placa é obrigatória")
    private String placa;
    @NotBlank(message = "Marca é obrigatória")
    private String marca;
    @NotBlank(message = "Modelo é obrigatório")
    private String modelo;
    @NotNull(message = "Ano é obrigatório")
    @Min(value = 1, message = "Ano deve ser positivo")
    private Integer ano;
    
    private String cor;
    public int getId() { return id; }
    public void setId(int value) { id = value; }
    public String getPlaca() { return placa; }
    public void setPlaca(String value) { placa = value; }
    public String getMarca() { return marca; }
    public void setMarca(String value) { marca = value; }
    public String getModelo() { return modelo; }
    public void setModelo(String value) { modelo = value; }
    public Integer getAno() { return ano; }
    public void setAno(Integer value) { ano = value; }
    public String getCor() { return cor; }
    public void setCor(String value) { cor = value; }
}

