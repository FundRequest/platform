export class Serializable {
  public fillFromJSON(json: any) {
    let jsonObj;

    if (typeof json !== 'object') {
      jsonObj = JSON.parse(json);
    } else {
      jsonObj = json;
    }

    console.log(jsonObj);

    for (var propName in jsonObj) {
      this[propName] = jsonObj[propName]
    }
  }
}
