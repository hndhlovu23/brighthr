import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Filetype e2e test', () => {
  const filetypePageUrl = '/filetype';
  const filetypePageUrlPattern = new RegExp('/filetype(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const filetypeSample = { type: 'Carolina' };

  let filetype;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/filetypes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/filetypes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/filetypes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (filetype) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/filetypes/${filetype.id}`,
      }).then(() => {
        filetype = undefined;
      });
    }
  });

  it('Filetypes menu should load Filetypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('filetype');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Filetype').should('exist');
    cy.url().should('match', filetypePageUrlPattern);
  });

  describe('Filetype page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(filetypePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Filetype page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/filetype/new$'));
        cy.getEntityCreateUpdateHeading('Filetype');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filetypePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/filetypes',
          body: filetypeSample,
        }).then(({ body }) => {
          filetype = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/filetypes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/filetypes?page=0&size=20>; rel="last",<http://localhost/api/filetypes?page=0&size=20>; rel="first"',
              },
              body: [filetype],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(filetypePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Filetype page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('filetype');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filetypePageUrlPattern);
      });

      it('edit button click should load edit Filetype page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Filetype');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filetypePageUrlPattern);
      });

      it('edit button click should load edit Filetype page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Filetype');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filetypePageUrlPattern);
      });

      it('last delete button click should delete instance of Filetype', () => {
        cy.intercept('GET', '/api/filetypes/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('filetype').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filetypePageUrlPattern);

        filetype = undefined;
      });
    });
  });

  describe('new Filetype page', () => {
    beforeEach(() => {
      cy.visit(`${filetypePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Filetype');
    });

    it('should create an instance of Filetype', () => {
      cy.get(`[data-cy="type"]`).type('impactful River cutting-edge').should('have.value', 'impactful River cutting-edge');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        filetype = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', filetypePageUrlPattern);
    });
  });
});
