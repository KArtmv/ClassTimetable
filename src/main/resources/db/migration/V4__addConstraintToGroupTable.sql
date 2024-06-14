ALTER TABLE groups
    ADD CONSTRAINT unique_group_name UNIQUE (group_name);
