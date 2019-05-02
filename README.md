Lista de End Points:
GET
Mapped "{[/oauth/token],methods=[GET]}"
Mapped "{[/oauth/authorize]}"
Mapped "{[/oauth/check_token]}"
Mapped "{[/oauth/confirm_access]}"
Mapped "{[/oauth/error]}"
Mapped "{[/error]}"
Mapped "{[/error],produces=[text/html]}"
Mapped "{[/oauth/token_key],methods=[GET]}"
Mapped "{[/categorias],methods=[GET]}"
Mapped "{[/categorias/{codigo}],methods=[GET]}"
Mapped "{[/lancamentos/{codigo}],methods=[GET]}"
Mapped "{[/lancamentos/relatorios/por-pessoa],methods=[GET]}"
Mapped "{[/lancamentos/estatistica/por-categoria],methods=[GET]}"
Mapped "{[/lancamentos],methods=[GET]}"
Mapped "{[/lancamentos],methods=[GET],params=[resumo]}"
Mapped "{[/lancamentos/estatistica/por-dia],methods=[GET]}
Mapped "{[/pessoas/{codigo}],methods=[GET]}"
Mapped "{[/pessoas],methods=[GET]}"

POST
Mapped "{[/oauth/authorize],methods=[POST],params=[user_oauth_approval]}"
Mapped "{[/oauth/token],methods=[POST]}"
Mapped "{[/categorias],methods=[POST]}"
Mapped "{[/lancamentos/anexo],methods=[POST]}"
Mapped "{[/lancamentos],methods=[POST]}"
Mapped "{[/pessoas],methods=[POST]}"

PUT
Mapped "{[/lancamentos/{codigo}],methods=[PUT]}"
Mapped "{[/pessoas/{codigo}/ativo],methods=[PUT]}"
Mapped "{[/pessoas/{codigo}],methods=[PUT]}"

DELETE
Mapped "{[/pessoas/{codigo}],methods=[DELETE]}"
Mapped "{[/lancamentos/{codigo}],methods=[DELETE]}"
Mapped "{[/tokens/revoke],methods=[DELETE]}"

Content-Type: application/json
Authorization: Bearer AcessTokenJWT

Para gerar um Acess Token JWT utilizar POST na URL:
Mapped "{[/oauth/token],methods=[POST]}"