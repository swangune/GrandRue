alter table booking_booking
    add column reservation_commitment_state text not null default 'IN_FORCE';

alter table booking_booking
    add constraint ck_booking_reservation_commitment_state
        check (reservation_commitment_state in ('IN_FORCE', 'RELEASED'));
