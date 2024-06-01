import json
import psycopg2

def lambda_handler(event, context):
    # Establishing the connection
    try:
        conn = psycopg2.connect(
        database='rng-dev', user='noob77777', password='hkSzhtwjCoI1SEQ', host='172.31.39.12', port= '5432'
        )
    except psycopg2.OperationalError as e:
        raise e
    # Setting auto commit false
    conn.autocommit = False
    
    # Creating a cursor object using the cursor() method
    cursor = conn.cursor()

    get_query = '''select * from rngdetails where task_id IN (%s)'''
    data = [event['taskId']]
    # Executing the query
    cursor.execute(get_query, data)
    
    result = cursor.fetchone()
    if result==None:
        # Preparing SQL query to INSERT a record into the database.
        query = (
        '''INSERT INTO rngdetails(task_id, locking_contract_address, locking_token_id,
    locking_token_amount, hash_box_id, commit_box_id, reveal_box_id,
    task_status) VALUES (%s, %s, %s, %s, %s, %s, %s, %s)'''
        )
        data = (event['taskId'], event['lockingContractAddress'], event['lockingTokenId'],
                event['lockingTokenAmount'], event['hashBoxId'], event['commitBoxId'],
                event['revealBoxId'], event['taskStatus'])
    else:
        query = (
            '''UPDATE rngdetails SET locking_contract_address = %s,
                locking_token_id = %s,
                locking_token_amount = %s,
                hash_box_id = %s,
                commit_box_id = %s,
                reveal_box_id = %s,
                task_status = %s
                WHERE task_id = %s'''
        )
        data = (event['lockingContractAddress'], event['lockingTokenId'],
                event['lockingTokenAmount'], event['hashBoxId'], event['commitBoxId'],
                event['revealBoxId'], event['taskStatus'], event['taskId'])

    try:
        # Executing the SQL command
        cursor.execute(query, data)
        # Commit your changes in the database
        conn.commit()
    except Exception as e:
        # Rolling back in case of error
        conn.rollback()
        raise e
    
    # Closing the connection
    conn.close()
    return event
