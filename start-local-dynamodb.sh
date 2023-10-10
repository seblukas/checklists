#bin/bash
docker ps | grep dynamodb-checkr
if [ $? -eq 0 ]; then
  echo "DynamoDB container is already running"
  exit 0
fi
aws configure set aws_access_key_id fake --profile dynamodb-checkr
aws configure set aws_secret_access_key fake --profile dynamodb-checkr
docker run -d -p 8000:8000 --name dynamodb-checkr amazon/dynamodb-local
aws dynamodb create-table \
  --table-name checklists \
  --attribute-definitions \
    AttributeName=PK,AttributeType=S \
    AttributeName=SK,AttributeType=S \
  --key-schema \
    AttributeName=PK,KeyType=HASH \
    AttributeName=SK,KeyType=RANGE \
  --provisioned-throughput \
    ReadCapacityUnits=5,WriteCapacityUnits=5 \
  --endpoint-url "http://localhost:8000" \
  --region "localhost" > /dev/null \
  --profile dynamodb-checkr
