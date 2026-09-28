package br.edu.unirv.garagem.repository;

import br.edu.unirv.garagem.model.Pessoa;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PessoaRepository implements IPessoaRepository {

    private static final String ARQUIVO = "data/pessoas.json";
    private final ObjectMapper mapper = new ObjectMapper();

    private List<Pessoa> carregar() {
        File arquivo = new File(ARQUIVO);
        if (!arquivo.exists()) {
            return new ArrayList<>();
        }
        try {
            return mapper.readValue(arquivo, new TypeReference<List<Pessoa>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private void salvar(List<Pessoa> pessoas) {
        try {
            File arquivo = new File(ARQUIVO);
            arquivo.getParentFile().mkdirs();
            mapper.writerWithDefaultPrettyPrinter().writeValue(arquivo, pessoas);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar pessoas.json", e);
        }
    }

    @Override
    public List<Pessoa> obterTodas() {
        return carregar();
    }

    @Override
    public Optional<Pessoa> obterPorId(int id) {
        return carregar().stream().filter(p -> p.getId() == id).findFirst();
    }

    @Override
    public void adicionar(Pessoa pessoa) {
        List<Pessoa> pessoas = carregar();
        int novoId = pessoas.stream().mapToInt(Pessoa::getId).max().orElse(0) + 1;
        pessoa.setId(novoId);
        pessoas.add(pessoa);
        salvar(pessoas);
    }

    @Override
    public void atualizar(Pessoa pessoa) {
        List<Pessoa> pessoas = carregar();
        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getId() == pessoa.getId()) {
                pessoas.set(i, pessoa);
                break;
            }
        }
        salvar(pessoas);
    }

    @Override
    public void remover(int id) {
        List<Pessoa> pessoas = carregar();
        pessoas.removeIf(p -> p.getId() == id);
        salvar(pessoas);
    }

    @Override
    public boolean cpfJaExiste(String cpf, int idIgnorado) {
        return carregar().stream()
                .filter(p -> p.getId() != idIgnorado)
                .anyMatch(p -> p.getCpf().equals(cpf));
    }
}
