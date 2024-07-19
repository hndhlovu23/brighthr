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

describe('File e2e test', () => {
  const filePageUrl = '/file';
  const filePageUrlPattern = new RegExp('/file(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const fileSample = { name: 'Salad', date: '2024-07-16T22:00:20.347Z', fileUrl: 'Fantastic frictionless payment' };

  let file;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/files+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/files').as('postEntityRequest');
    cy.intercept('DELETE', '/api/files/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (file) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/files/${file.id}`,
      }).then(() => {
        file = undefined;
      });
    }
  });

  it('Files menu should load Files page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('file');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('File').should('exist');
    cy.url().should('match', filePageUrlPattern);
  });

  describe('File page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(filePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create File page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/file/new$'));
        cy.getEntityCreateUpdateHeading('File');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/files',
          body: fileSample,
        }).then(({ body }) => {
          file = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/files+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/files?page=0&size=20>; rel="last",<http://localhost/api/files?page=0&size=20>; rel="first"',
              },
              body: [file],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(filePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details File page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('file');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filePageUrlPattern);
      });

      it('edit button click should load edit File page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('File');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filePageUrlPattern);
      });

      it('edit button click should load edit File page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('File');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filePageUrlPattern);
      });

      it('last delete button click should delete instance of File', () => {
        cy.intercept('GET', '/api/files/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('file').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', filePageUrlPattern);

        file = undefined;
      });
    });
  });

  describe('new File page', () => {
    beforeEach(() => {
      cy.visit(`${filePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('File');
    });

    it('should create an instance of File', () => {
      cy.get(`[data-cy="name"]`).type('Washington integrated California').should('have.value', 'Washington integrated California');

      cy.get(`[data-cy="date"]`).type('2024-07-16T23:07').blur().should('have.value', '2024-07-16T23:07');

      cy.get(`[data-cy="fileUrl"]`).type('Officer').should('have.value', 'Officer');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        file = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', filePageUrlPattern);
    });
  });
});
