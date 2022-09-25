# springboot, react, social login, kotlin, jvm 17

## Requirements
- java 17
- node 18
- npm 8
- docker

## commands
- `make recreate-database`
- `make test-api`
- `make run-api-8080` or `make run-api-3000`
- `make run-ui`

## Help
- [react-icons](https://react-icons.github.io/react-icons)

## sql

```mysql-sql
select *
  from organization_share
 where organization_id = UUID_TO_BIN('adbafedb-8dab-4a58-9825-db9e73c89f32')
```
