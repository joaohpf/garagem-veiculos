# Validar a Entrega 2

1. Na raiz, execute `mvn test`. Se falhar, guarde a saída para corrigir antes do envio.
2. Execute `mvn spring-boot:run` e abra http://localhost:8080.
3. Confirme que abre Reservas. Navegue também em Pessoas e Veículos.
4. Em Veículos, cadastre, edite e exclua um veículo de teste.
5. Tente cadastrar a placa ABC1D23 e abc-1d23: deve exibir placa duplicada.
6. Envie formulário com campos vazios: deve retornar mensagens.
7. Há uma reserva de exemplo para ABC1D23 / Ana Souza em 10/10/2026 a 15/10/2026.
8. Tente reservar o mesmo veículo em 14/10 a 18/10: deve bloquear.
9. Tente em 15/10 a 17/10: deve bloquear (fim inclusivo).
10. Tente em 01/10 a 31/10: deve bloquear.
11. Tente em 16/10 a 20/10: deve permitir. Cancele essa reserva depois.
12. Reserve outro veículo no mesmo período: deve permitir.
13. Edite a reserva original mantendo as datas: deve permitir, sem conflito consigo mesma.
14. Edite para um período que colida com outra reserva do mesmo veículo: deve bloquear.
15. Tente fim anterior ao início: deve exibir erro.
16. Crie uma reserva incluindo hoje: esse veículo deve aparecer Reservado. Cancele e confira Disponível.
17. Cadastre um veículo e uma reserva, pare com Ctrl+C e reinicie: ambos devem continuar lá.

## Prints obrigatórios

- Página inicial com as reservas, pessoas, veículos, período e status.
- Tentativa de reserva em conflito com mensagem de bloqueio visível.

Salve os prints reais em `docs/reservas.png` e `docs/conflito.png`.
A primeira entrega também exige prints da listagem e formulário de Pessoas.
Não há prints de execução incluídos nesta atualização.

## Sua contribuição no GitHub

Trabalhe na pasta do clone do grupo. Copie os arquivos desta atualização, preservando
o diretório .git do clone e os dados reais do grupo em data/.
Use `git diff` antes de confirmar. Não substitua pessoas.json se tiver novos cadastros.
O JSON de veículos e reservas fornecido é exemplo; não sobrescreva dados reais existentes.

```sh
git switch -c entrega-2-gustavo
git status
git diff
git add src pom.xml README.md docs data/veiculos.json data/reservas.json
git commit -m "Implementa veículos e reservas com persistência JSON"
git push -u origin entrega-2-gustavo
```

Abra um Pull Request para master e combine a integração com o grupo.
Configure nome e email do Git antes do commit para registrar sua autoria.
O enunciado exige que todos enviem o mesmo link no Classroom; não exige um commit por integrante.

