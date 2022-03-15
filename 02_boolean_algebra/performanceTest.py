import requests
import json
import csv

#with open('bruteForceGuava.csv', 'w', newline='') as file:
#with open('bruteForcePyhon.csv', 'w', newline='') as file:
with open('dnfTest.csv', 'w', newline='') as file:
    writer = csv.writer(file)
    writer.writerow(["depth", "decToOutputEdges", "inputToDecEdges", "outputToDecEdges", "numberOfNodes", "numberOfEdges", "numberOfInputNodes", "numberOfOutputNodes", "numberOfDecisionNodes", "booleanExprTime", "graphBuildingTime", "dnf_time"])
    
    with open('parameters.csv') as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=';')
        line_count = 0
        for row in csv_reader:
            if line_count > 0:
                depth = row[0]
                decToOutputEdges = row[1]
                inputToDecEdges = row[2]
                outputToDecEdges = row[3]               

                url = "http://192.168.178.44:8080/dmnapi/v1/graph/bruteForcePerformanceTest/?depth=" + str(depth) + "&decToOutputEdges=" + str(decToOutputEdges) + "&inputToDecEdges=" + str(inputToDecEdges) + "&outputToDecEdges=" + str(outputToDecEdges)
                
                #url = "http://192.168.178.44:8080/dmnapi/v1/graph/performanceTest/?depth=" + str(depth) + "&decToOutputEdges=" + str(decToOutputEdges) + "&inputToDecEdges=" + str(inputToDecEdges) + "&outputToDecEdges=" + str(outputToDecEdges)

                payload={}
                headers = {}

                response = requests.request("POST", url, headers=headers, data=payload)

                if response.status_code == 200:
                    res = json.loads(response.text)     
                    row = [depth, decToOutputEdges, inputToDecEdges, outputToDecEdges, res["numberOfNodes"], res["numberOfEdges"], res["numberOfInputNodes"], res["numberOfOutputNodes"], res["numberOfDecisionNodes"], res["booleanExprTime"], res["graphBuildingTime"], res["dnf_time"]]                   
                    writer.writerow(row)
                    print(row)
                else:
                    row = [depth, decToOutputEdges, inputToDecEdges, outputToDecEdges]                   
                    writer.writerow(row)
                    print(row)
            line_count += 1