CREATE TABLE public.rngdetails (
    task_id character varying(255) NOT NULL,
    locking_contract_address character varying(255),
    locking_token_id character varying(255),
    locking_token_amount integer,
    hash_box_id character varying(255),
    commit_box_id character varying(255),
    reveal_box_id character varying(255),
    task_status character varying(255),
    CONSTRAINT check_status CHECK (((task_status)::text = ANY ((ARRAY['NOT_STARTED'::character varying, 'COMMITTED'::character varying, 'REVEAL_IN_PROGRESS'::character varying, 'COMPLETED'::character varying, 'COMMIT_IN_PROGRESS'::character varying])::text[])))
);