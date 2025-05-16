import * as jspb from 'google-protobuf'



export class Empty extends jspb.Message {
  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): Empty.AsObject;
  static toObject(includeInstance: boolean, msg: Empty): Empty.AsObject;
  static serializeBinaryToWriter(message: Empty, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): Empty;
  static deserializeBinaryFromReader(message: Empty, reader: jspb.BinaryReader): Empty;
}

export namespace Empty {
  export type AsObject = {
  }
}

export class LocationRequest extends jspb.Message {
  getUserid(): string;
  setUserid(value: string): LocationRequest;

  getLatitude(): number;
  setLatitude(value: number): LocationRequest;

  getLongitude(): number;
  setLongitude(value: number): LocationRequest;

  getTimestamp(): number;
  setTimestamp(value: number): LocationRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): LocationRequest.AsObject;
  static toObject(includeInstance: boolean, msg: LocationRequest): LocationRequest.AsObject;
  static serializeBinaryToWriter(message: LocationRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): LocationRequest;
  static deserializeBinaryFromReader(message: LocationRequest, reader: jspb.BinaryReader): LocationRequest;
}

export namespace LocationRequest {
  export type AsObject = {
    userid: string,
    latitude: number,
    longitude: number,
    timestamp: number,
  }
}

export class UserIdRequest extends jspb.Message {
  getUserid(): string;
  setUserid(value: string): UserIdRequest;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): UserIdRequest.AsObject;
  static toObject(includeInstance: boolean, msg: UserIdRequest): UserIdRequest.AsObject;
  static serializeBinaryToWriter(message: UserIdRequest, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): UserIdRequest;
  static deserializeBinaryFromReader(message: UserIdRequest, reader: jspb.BinaryReader): UserIdRequest;
}

export namespace UserIdRequest {
  export type AsObject = {
    userid: string,
  }
}

export class LocationResponse extends jspb.Message {
  getUserid(): string;
  setUserid(value: string): LocationResponse;

  getLatitude(): number;
  setLatitude(value: number): LocationResponse;

  getLongitude(): number;
  setLongitude(value: number): LocationResponse;

  getTimestamp(): number;
  setTimestamp(value: number): LocationResponse;

  serializeBinary(): Uint8Array;
  toObject(includeInstance?: boolean): LocationResponse.AsObject;
  static toObject(includeInstance: boolean, msg: LocationResponse): LocationResponse.AsObject;
  static serializeBinaryToWriter(message: LocationResponse, writer: jspb.BinaryWriter): void;
  static deserializeBinary(bytes: Uint8Array): LocationResponse;
  static deserializeBinaryFromReader(message: LocationResponse, reader: jspb.BinaryReader): LocationResponse;
}

export namespace LocationResponse {
  export type AsObject = {
    userid: string,
    latitude: number,
    longitude: number,
    timestamp: number,
  }
}

