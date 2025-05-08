# Liquibase Best Practices Guide

## Introduction

This guide outlines the best practices for managing database changes using Liquibase in our project. Liquibase helps us
track, version, and deploy database changes in a systematic way.

## Best Practices

### MANDATORY Naming Conventions

- Use meaningful names for descriptions changesets and changelog files
- Follow pattern: YYYYMMDD_NNN_description.sql
- Use lowercase for filenames
- Separate words with underscores

### Examples

20240501_001_crear_tabla_usuarios.sql
20240501_002_agregar_columna_email.sql
20240502_001_crear_tabla_productos.sql

### Changelog Organization

- One change per changeset
- Include rollback commands when possible
- Add clear comments describing changes
- Use labels and contexts appropriately
- Include author tags

### Version Control

- Never modify existing changesets
- Create new changesets for modifications
- Keep changelog files in version control
- Review changes before deployment
