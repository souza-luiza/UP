/// <reference types="cypress" />

Cypress.Commands.add('register', (name, email, password) => {
  cy.visit('/register');
  
  // Mapeado de acordo com o seu RegisterPage real
  cy.get('input[placeholder*="nome"]').type(name);
  cy.get('input[type="email"]').type(email);
  cy.get('input[placeholder="Escreva aqui a sua senha"]').type(password);
  cy.get('input[placeholder="Confirme a sua senha"]').type(password);
  
  cy.get('button[type="submit"]').click();
  
  // Aguarda o Next.js mudar para a tela de login
  cy.url().should('include', '/login');
});

Cypress.Commands.add('login', (email, password) => {
  cy.visit('/login');
  
  // Mapeado de acordo com o seu LoginPage real
  cy.get('input[type="email"]').type(email);
  cy.get('input[type="password"]').type(password);
  
  cy.get('button[type="submit"]').click();
  
  // Seu LoginPage faz: router.push("/perfil")
  cy.url().should('include', '/perfil');
});

declare global {
  namespace Cypress {
    interface Chainable {
      register(name: string, email: string, password: string): Chainable<void>;
      login(email: string, password: string): Chainable<void>;
    }
  }
}