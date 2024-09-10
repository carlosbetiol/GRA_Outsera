CREATE INDEX "idx_producer_name" ON "producer" ("name");

CREATE INDEX "idx_studio_name" ON "studio" ("name");

CREATE INDEX "idx_movie_name" ON "movie" ("name");

CREATE INDEX "idx_movie_award_name" ON "movie_award" ("award_year");

CREATE INDEX "idx_persistence_log_request_uuid" ON "persistence_log" ("request_uuid");

CREATE INDEX "idx_persistence_log_entity_name" ON "persistence_log" ("entity_name");

CREATE INDEX "idx_persistence_log_user_id" ON "persistence_log" ("user_id");

CREATE INDEX "idx_persistence_log_action" ON "persistence_log" ("action");

CREATE INDEX "idx_request_log_user_id" ON "request_log" ("user_id");

CREATE INDEX "idx_request_log_request_uuid" ON "request_log" ("request_uuid");

CREATE INDEX "idx_request_log_method" ON "request_log" ("method");

