import requests
import json

# Decisions

with open("decisions.json") as decision_file:
    decisions = json.load(decision_file) 
    url = "http://192.168.178.44:8080/dmnapi/v1/decision/"   
    headers = {
    'Content-Type': 'application/json'
    }
    for decision in decisions:
        payload = json.dumps({
        "name": decision['name'],
        "description": decision['description']
        })
        response = requests.request("POST", url, headers=headers, data=payload)
        print(response.text)
        
with open("inputdata.txt") as input_file:
    inputs = input_file.readlines() 
    url = "http://192.168.178.44:8080/dmnapi/v1/inputData/"
    headers = {
    'Content-Type': 'application/json'
    }
    for input in inputs:
        payload = json.dumps({
        "name": input.split(" ### ")[0].strip(),
        "description": input.split(" ### ")[1].strip()
        })
        response = requests.request("POST", url, headers=headers, data=payload)
        print(response.text)
        
with open("output.json") as output_file:
    outputs = json.load(output_file) 
    url = "http://192.168.178.44:8080/dmnapi/v1/outputData/"   
    headers = {
    'Content-Type': 'application/json'
    }
    for output in outputs:
        payload = json.dumps({
        "name": output['name'],
        "description": output['description']
        })
        response = requests.request("POST", url, headers=headers, data=payload)
        print(response.text)
        
with open("businessKnowledge.json") as businessKnowledge_file:
    businessKnowledges = json.load(businessKnowledge_file) 
    url = "http://192.168.178.44:8080/dmnapi/v1/businessKnowledge/"   
    headers = {
    'Content-Type': 'application/json'
    }
    for businessKnowledge in businessKnowledges:
        payload = json.dumps({
        "name": businessKnowledge['name'],
        "description": businessKnowledge['description'],
        "url": "https://www.omg.org/spec/DMN/1.3/PDF"
        })
        response = requests.request("POST", url, headers=headers, data=payload)
        print(response.text)
        
with open("knowledgesource.txt") as knowledgesource_file:
    knowledgesources = knowledgesource_file.readlines() 
    url = "http://192.168.178.44:8080/dmnapi/v1/knowledgeSource/"
    headers = {
    'Content-Type': 'application/json'
    }
    for knowledgesource in knowledgesources:
        payload = json.dumps({
        "name": knowledgesource.split(" ### ")[0].strip(),
        "description": knowledgesource.split(" ### ")[1].strip(),
        "url": "https://www.omg.org/spec/DMN/1.3/PDF"
        })
        response = requests.request("POST", url, headers=headers, data=payload)
        print(response.text)
        
with open("organization.txt") as organization_file:
    organizations = organization_file.readlines() 
    url = "http://192.168.178.44:8080/dmnapi/v1/organisationalUnit/"
    headers = {
    'Content-Type': 'application/json'
    }
    for organization in organizations:
        payload = json.dumps({
        "name": organization.split(" ### ")[0].strip(),
        "description": organization.split(" ### ")[1].strip(),
        "url": "https://www.omg.org/spec/DMN/1.3/PDF"
        })
        response = requests.request("POST", url, headers=headers, data=payload)
        print(response.text)