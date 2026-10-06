package br.edu.unirv.garagem.model;
import jakarta.validation.constraints.*;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
public class Reserva {
    
    private int id;
    @NotNull(message = "Selecione um veículo")
    private Integer veiculoId;
    @NotNull(message = "Selecione uma pessoa")
    private Integer pessoaId;
    @NotNull(message = "Data de início é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataInicio;
    @NotNull(message = "Data de fim é obrigatória")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private LocalDate dataFim;
    public int getId() { return id; }
    public void setId(int value) { id = value; }
    public Integer getVeiculoId() { return veiculoId; }
    public void setVeiculoId(Integer value) { veiculoId = value; }
    public Integer getPessoaId() { return pessoaId; }
    public void setPessoaId(Integer value) { pessoaId = value; }
    public LocalDate getDataInicio() { return dataInicio; }
    public void setDataInicio(LocalDate value) { dataInicio = value; }
    public LocalDate getDataFim() { return dataFim; }
    public void setDataFim(LocalDate value) { dataFim = value; }
}

