--liquibase formatted sql
--changeset test_2r:2

DROP VIEW IF EXISTS figure_max_area;

CREATE VIEW figure_max_area AS
SELECT id, 'circle' AS typ, pi() * radius * radius AS area FROM circle
UNION ALL
SELECT id, 'square' AS typ, side * side AS area FROM square
UNION ALL
SELECT id, 'rectangle' AS typ, width * height AS area FROM rectangle;