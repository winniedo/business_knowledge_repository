import requests
import json

url = "http://192.168.178.44:8080/dmnapi/v1/decision/"
response = requests.request("GET", url, headers={}, data={})
decisionList = json.loads(response.text)
decisionDict = {}
for dec in decisionList:
    decisionDict[dec["name"]] = str(dec["id"])

url = "http://192.168.178.44:8080/dmnapi/v1/inputData/"
response = requests.request("GET", url, headers={}, data={})
inputList = json.loads(response.text)
inputDict = {}
for inp in inputList:
    inputDict[inp["name"]] = str(inp["id"])

url = "http://192.168.178.44:8080/dmnapi/v1/outputData/"
response = requests.request("GET", url, headers={}, data={})
inputList = json.loads(response.text)
outputDict = {}
for outp in inputList:
    outputDict[outp["name"]] = str(outp["id"])
    
with open("01_db_dump_wo_id.json") as decision_file:
    decisions = json.load(decision_file) 
    urlBase = "http://192.168.178.44:8080/dmnapi/v1/decision/"   
    headers = {
    'Content-Type': 'application/json'
    }
    for decision in decisions:
        for inp in decision["inputData"]:
            url = urlBase + "inputData/?decisionId=" + decisionDict[decision["name"]] + "&inputDataId=" + inputDict[inp["name"]]
            print(url)
            response = requests.request("PUT", url, headers=headers, data={})
            print(response.text)
        for outp in decision["outputAsInputData"]:
            url = urlBase + "outputAsInputData/?decisionId=" + decisionDict[decision["name"]] + "&outputAsInputDataId=" + outputDict[outp["name"]]
            print(url)
            response = requests.request("PUT", url, headers=headers, data={})
            print(response.text)
        for outp in decision["outputData"]:
            url = urlBase + "outputData/?decisionId=" + decisionDict[decision["name"]] + "&outputDataId=" + outputDict[outp["name"]]
            print(url)
            response = requests.request("PUT", url, headers=headers, data={})
            print(response.text)