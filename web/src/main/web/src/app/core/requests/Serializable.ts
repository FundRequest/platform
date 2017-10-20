export class Serializable {
  fillFromJSON(json: any) {
    let jsonObj;

    if (typeof json !== 'object') {
      jsonObj = JSON.parse(json);
    } else {
      jsonObj = json;
    }

    for (var propName in jsonObj) {
      this[propName] = jsonObj[propName]
    }
  }
}
