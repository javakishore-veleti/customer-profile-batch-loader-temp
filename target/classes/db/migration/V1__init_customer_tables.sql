CREATE TABLE customer_profile_batch (
                                        batch_id VARCHAR(64) PRIMARY KEY,
                                        created_date_time TIMESTAMP,
                                        batch_number INTEGER,
                                        profile_ids_count INTEGER,
                                        data_sync_status VARCHAR(20),
                                        data_sync_purpose INTEGER,
                                        batch_request_date_time TIMESTAMP
);

CREATE TABLE customer_profile (
                                  profile_id VARCHAR(64) PRIMARY KEY,
                                  data_sync_status VARCHAR(20),
                                  batch_id VARCHAR(64),
                                  created_date_time TIMESTAMP,
                                  CONSTRAINT fk_batch_id FOREIGN KEY (batch_id)
                                      REFERENCES customer_profile_batch (batch_id)
);
