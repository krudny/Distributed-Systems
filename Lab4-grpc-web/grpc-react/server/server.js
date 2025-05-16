const grpc = require('@grpc/grpc-js');
const protoLoader = require('@grpc/proto-loader');
const path = require('path');

const PROTO_PATH = path.join(__dirname, 'proto', 'location.proto');

const packageDefinition = protoLoader.loadSync(PROTO_PATH, {
  keepCase: true,
  longs: String,
  enums: String,
  defaults: true,
  oneofs: true,
});
const locationProto = grpc.loadPackageDefinition(packageDefinition).location;

const subscribers = new Map();      // userId -> call
const usersLocations = new Map();   // userId -> { latitude, longitude, timestamp }

// Implementacja unary RPC: SendLocation
function sendLocation(call, callback) {
  const req = call.request;
  const { userId, latitude, longitude, timestamp } = req;

  // Zapis lokalizacji
  usersLocations.set(userId, { latitude, longitude, timestamp });
  console.log(`Received location from user ${userId}:`, { latitude, longitude, timestamp });

  // Rozsyłanie aktualizacji do wszystkich subskrybentów
  const update = { userId, latitude, longitude, timestamp };
  for (const [subscriberId, subscriberCall] of subscribers) {
    console.log(`Broadcasting update to subscriber ${subscriberId}:`, update);
    subscriberCall.write(update);
  }

  callback(null, {});
}

// Implementacja server-stream RPC: SubscribeLocations
function subscribeLocations(call) {
  console.log('Invoked SubscribeLocations');

  const userId = call.request.userId;

  if (!userId) {
    console.error('SubscribeLocations: No userId provided in the request.');
    call.end(grpc.status.INVALID_ARGUMENT, 'userId is required');
    return;
  }

  subscribers.set(userId, call);
  console.log(`User ${userId} subscribed to location updates`);
  console.log(usersLocations);

  for (const [otherId, loc] of usersLocations) {
    console.log(`Sending existing location of ${otherId} to new subscriber ${userId}:`, loc);
    call.write({ userId: otherId, ...loc });
  }

  call.on('end', () => {
    console.log(`Call from ${userId} ended.`);
    if (subscribers.has(userId) && subscribers.get(userId) === call) {
      subscribers.delete(userId);
      console.log(`User ${userId} unsubscribed from updates`);
    }
  });

  call.on('error', (err) => {
    console.error(`SubscribeLocations error for user ${userId}:`, err);
    if (subscribers.has(userId) && subscribers.get(userId) === call) {
      subscribers.delete(userId);
      console.log(`User ${userId} unsubscribed due to error`);
    }
  });

  call.on('cancelled', () => {
    console.log(`Call from ${userId} was cancelled.`);
    if (subscribers.has(userId) && subscribers.get(userId) === call) {
      subscribers.delete(userId);
      console.log(`User ${userId} unsubscribed due to cancellation`);
    }
  });
}



// Tworzenie serwera
function getServer() {
  const server = new grpc.Server();
  server.addService(locationProto.LocationService.service, {
    SendLocation: sendLocation,
    SubscribeLocations: subscribeLocations,
  });
  return server;
}

// Uruchomienie serwera
const server = getServer();
const bindAddress = '0.0.0.0:50051';
server.bindAsync(bindAddress, grpc.ServerCredentials.createInsecure(), (err, port) => {
  if (err) {
    console.error('Server error:', err);
    return;
  }
  console.log(`gRPC Server started on ${bindAddress}`);
  server.start();
});

// Obsługa wyłączenia
process.on('SIGTERM', () => {
  console.log('SIGTERM received, shutting down gRPC server...');
  server.tryShutdown((err) => {
    if (err) console.error('Error during shutdown:', err);
    else console.log('gRPC server shut down cleanly');
  });
});

process.on('SIGINT', () => {
  console.log('SIGINT received, shutting down gRPC server...');
  server.tryShutdown((err) => {
    if (err) console.error('Error during shutdown:', err);
    else console.log('gRPC server shut down cleanly');
  });
});