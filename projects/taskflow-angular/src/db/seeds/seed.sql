-- Seed data for TaskFlow Angular
INSERT INTO projects (name, description, created_at, updated_at) VALUES
  ('Website Redesign', 'Full redesign of the company website', NOW(), NOW()),
  ('Mobile App', 'Cross-platform mobile application', NOW(), NOW()),
  ('API Integration', 'Third-party API integrations', NOW(), NOW());

INSERT INTO tasks (title, description, status, priority, project_id, created_at, updated_at) VALUES
  ('Design mockups', 'Create Figma mockups for all pages', 'TODO', 'HIGH', 1, NOW(), NOW()),
  ('Set up CI/CD', 'Configure GitHub Actions pipeline', 'IN_PROGRESS', 'HIGH', 1, NOW(), NOW()),
  ('Write unit tests', 'Achieve 80% test coverage', 'TODO', 'MEDIUM', 1, NOW(), NOW()),
  ('Build login screen', 'Email + social login', 'IN_PROGRESS', 'HIGH', 2, NOW(), NOW()),
  ('Push notifications', 'FCM integration', 'TODO', 'LOW', 2, NOW(), NOW()),
  ('OAuth 2.0 setup', 'Integrate OAuth provider', 'DONE', 'HIGH', 3, NOW(), NOW()),
  ('Rate limiting', 'Add rate limiting middleware', 'TODO', 'MEDIUM', 3, NOW(), NOW());
