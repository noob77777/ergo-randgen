import json
import psycopg2

def lambda_handler(event, context):
    try:
        conn = psycopg2.connect(
        database='rng-dev', user='noob77777', password='{password}', host='172.31.39.12', port='5432'
        )
    except psycopg2.OperationalError as e:
        return {
            "statusCode": 502,
            "body": json.dumps('DB connection failed')
        }
    cursor = conn.cursor()

    get_query = '''select * from rngdetails where task_id IN (%s)'''
    data = [event["pathParameters"]["taskId"]]
    # Executing the query
    cursor.execute(get_query, data)

    result = cursor.fetchone()
    if result == None:
        return {
            "statusCode": 200,
            "body": json.dumps('Does not exist!')
        }

    rngDetail = {
        "taskId": result[0],
        "lockingContractAddress": result[1],
        "lockingTokenId": result[2],
        "lockingTokenAmount": result[3],
        "hashBoxId": result[4],
        "commitBoxId": result[5],
        "revealBoxId": result[6],
        "taskStatus":result[7]
    }

    # Closing the connection
    conn.close()
    return {
        "statusCode": 200,
        "body": json.dumps(rngDetail)
    }
