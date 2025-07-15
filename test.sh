#!/bin/bash

BASE_URL="http://localhost"
USER_SERVICE="$BASE_URL:8081"
CATEGORY_SERVICE="$BASE_URL:8082"
MEME_SERVICE="$BASE_URL:8083"

echo "🚀 Testando Memelândia APIs..."

echo "📝 1. Criando usuário..."
USER_RESPONSE=$(curl -s -X POST "$USER_SERVICE/usuarios" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "email": "joao@memelandia.com"
  }')

USER_ID=$(echo $USER_RESPONSE | jq -r '.id')
echo "✅ Usuário criado com ID: $USER_ID"

echo "📝 2. Criando categoria..."
CATEGORY_RESPONSE=$(curl -s -X POST "$CATEGORY_SERVICE/categorias" \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Memes Engraçados",
    "descricao": "Os memes mais engraçados da internet"
  }')

CATEGORY_ID=$(echo $CATEGORY_RESPONSE | jq -r '.id')
echo "✅ Categoria criada com ID: $CATEGORY_ID"

echo "📝 3. Criando meme..."
MEME_RESPONSE=$(curl -s -X POST "$MEME_SERVICE/memes" \
  -H "Content-Type: application/json" \
  -d "{
    \"nome\": \"Meme Teste\",
    \"descricao\": \"Um meme de teste muito engraçado\",
    \"urlImagem\": \"https://example.com/meme.jpg\",
    \"usuarioId\": $USER_ID,
    \"categoriaId\": $CATEGORY_ID
  }")

MEME_ID=$(echo $MEME_RESPONSE | jq -r '.id')
echo "✅ Meme criado com ID: $MEME_ID"

echo "📝 4. Testando meme do dia..."
curl -s "$MEME_SERVICE/memes/meme-do-dia" | jq .
echo "✅ Meme do dia obtido"

echo "📝 5. Listando todos os memes..."
curl -s "$MEME_SERVICE/memes" | jq .
echo "✅ Memes listados"

echo "📝 6. Verificando health dos serviços..."
echo "User Service Health:"
curl -s "$USER_SERVICE/actuator/health" | jq .

echo "Category Service Health:"
curl -s "$CATEGORY_SERVICE/actuator/health" | jq .

echo "Meme Service Health:"
curl -s "$MEME_SERVICE/actuator/health" | jq .

echo "🎉 Testes concluídos!"