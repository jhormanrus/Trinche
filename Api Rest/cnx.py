from flask import Flask, jsonify, json
import cx_Oracle

#conn = cx_Oracle.connect('gth/123@192.168.21.9:1521/xe')
conn = cx_Oracle.connect('gth/123@localhost:1521/xe')
cur = conn.cursor()
cur.execute('SELECT * FROM RHMV_TRABAJADOR_FILTRADO')
row_headers=[x[0] for x in cur.description]
rv = cur.fetchall()
json_data=[]
for line in rv:
    json_data.append(dict(zip(row_headers,line)))
    print(json.dumps(json_data))
cur.close()
conn.close()