export class EventWithContent<T> {
  name: string;
  content: T;

  constructor(name: string, content: T) {
    this.name = name;
    this.content = content;
  }
}
