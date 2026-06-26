describe('Jornada do Novo Usuário (E2E Happy Path)', () => {
    const userName = 'Test User';
    const userPassword = 'TestPassword123!';

    it('deve permitir que um novo usuário se registre, faça login e acesse o perfil', () => {
        // Gera um e-mail único baseado no timestamp exato desta execução
        const emailAleatorio = `user_${Date.now()}@test.com`;

        // 1. Visitar a página de registro e preencher os campos reais
        cy.visit('/register');
        cy.get('input[placeholder*="nome"]').type(userName);
        cy.get('input[type="email"]').type(emailAleatorio);
        cy.get('input[placeholder="Escreva aqui a sua senha"]').type(userPassword);
        cy.get('input[placeholder="Confirme a sua senha"]').type(userPassword);
        cy.get('button[type="submit"]').click();

        // 2. Verificar redirecionamento para a tela de login
        cy.url().should('include', '/login');

        // 3. Preencher o formulário de login com o e-mail que acabou de ser criado
        cy.get('input[type="email"]').type(emailAleatorio);
        cy.get('input[type="password"]').type(userPassword);
        cy.get('button[type="submit"]').click();

        // 4. Verificar se o login teve sucesso e redirecionou para o perfil
        cy.url().should('include', '/perfil');

        // 5. Fluxo de Disciplinas
        // 5. Fluxo de Disciplinas
        cy.visit('/disciplinas');

        // ALTERADO: Substitui os data-cy genéricos por seletores baseados no texto/placeholder real
        cy.get('input[placeholder*="nome"]').type('Banco de Dados'); // ou o placeholder real que usou para a matéria
        cy.get('#nota-5').check(); // Seleciona a dificuldade no dropdown existente
        cy.get('button[type="submit"]').click(); // Clica no botão de cadastrar disciplina
        cy.contains('Banco de Dados').should('be.visible');

        cy.get('input[placeholder*="nome"]').clear().type('POO');
        cy.get('#nota-3').check();
        cy.get('button[type="submit"]').click();
        cy.contains('POO').should('be.visible');

        // 6. Fluxo de Disponibilidades (Ajustado para a rota real no plural)
        cy.visit('/disponibilidades');
        cy.get('[data-cy="disponibilidade-dia-select"]').select('Segunda-feira');
        cy.get('[data-cy="disponibilidade-inicio-time-input"]').type('19:00');
        cy.get('[data-cy="disponibilidade-fim-time-input"]').type('21:00');
        cy.get('[data-cy="add-disponibilidade-button"]').click();
        cy.contains('Segunda-feira: 19:00 - 21:00').should('be.visible');

        // 7. Geração e validação do Cronograma
        cy.visit('/cronograma');
        cy.get('[data-cy="gerar-cronograma-button"]').click();

        // Aguarda a resposta do algoritmo de geração
        cy.contains('Sessões de Estudo Geradas').should('be.visible');

        // Valida se as disciplinas foram distribuídas na tela
        cy.get('[data-cy="sessao-estudo-card"]').contains('Banco de Dados').should('exist');
        cy.get('[data-cy="sessao-estudo-card"]').contains('POO').should('exist');

        // Lógica de cálculo dos minutos alocados por disciplina
        let bdMinutes = 0;
        let pooMinutes = 0;

        const extractMinutes = (text: string) => {
            const match = text.match(/(\d+)\s*minutos/);
            return match ? parseInt(match[1], 10) : 0;
        };

        cy.get('[data-cy="sessao-estudo-card"]:contains("Banco de Dados")').each(($el) => {
            const text = $el.text();
            bdMinutes += extractMinutes(text);
        });

        cy.get('[data-cy="sessao-estudo-card"]:contains("POO")').each(($el) => {
            const text = $el.text();
            pooMinutes += extractMinutes(text);
        }).then(() => {
            cy.log(`Minutos de Banco de Dados: ${bdMinutes}`);
            cy.log(`Minutos de POO: ${pooMinutes}`);

            // Regras de negócio do cronograma: tempos válidos e ordenação por dificuldade
            expect(bdMinutes).to.be.greaterThan(0);
            expect(pooMinutes).to.be.greaterThan(0);
            expect(bdMinutes).to.be.greaterThan(pooMinutes);
        });
    });
});