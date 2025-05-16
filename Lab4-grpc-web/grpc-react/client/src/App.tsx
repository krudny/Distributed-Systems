import React, { useState, useEffect, useMemo } from 'react';
import { LocationServiceClient } from './proto/LocationServiceClientPb';
import { UserIdRequest, LocationResponse, LocationRequest } from './proto/location_pb';
import * as grpcWeb from 'grpc-web';

interface LocationData {
  latitude: number;
  longitude: number;
  timestamp: number;
}

const EnvoyURL = "http://localhost:8000";

function App() {
  const [myLocation, setMyLocation] = useState<LocationData | null>(null);
  const [otherLocations, setOtherLocations] = useState<Record<string, LocationData>>({});
  const [errorMessage, setErrorMessage] = useState<string | null>(null);
  const [streamStatus, setStreamStatus] = useState<'idle' | 'connecting' | 'connected' | 'error' | 'ended'>('idle');
  const [locationSentSuccess, setLocationSentSuccess] = useState<boolean>(false);
  const client = new LocationServiceClient(EnvoyURL);

  const userId = useMemo(() => `simple-user-${Math.floor(Math.random() * 10000)}`, []);

  const handleSendLocation = () => {
    setErrorMessage(null);
    setLocationSentSuccess(false);

    const exampleLatitude = 52.2441;
    const exampleLongitude = 21.0225;
    const timestamp = Date.now();

    const locationData: LocationData = {
      latitude: exampleLatitude,
      longitude: exampleLongitude,
      timestamp: timestamp
    };

    setMyLocation(locationData);

    console.log(`[${userId}] Using example location: Lat ${locationData.latitude.toFixed(6)}, Lng ${locationData.longitude.toFixed(6)}. Sending via SendLocation RPC.`);



    const request = new LocationRequest();
    request.setUserid(userId);
    request.setLatitude(locationData.latitude);
    request.setLongitude(locationData.longitude);
    request.setTimestamp(locationData.timestamp);

    const metadata = {};

    client.sendLocation(request, metadata, (err, response) => {
      if (err) {
        console.error(`[${userId}] SendLocation RPC Error: Code ${err.code}, Message: ${err.message}`);
        setErrorMessage(`Failed to send location: ${err.message} (Code: ${err.code})`);
        setLocationSentSuccess(false);
      } else {
        console.log(`[${userId}] SendLocation RPC Success. Subscribing now.`);
        setLocationSentSuccess(true);
      }
    });
  };

  useEffect(() => {
    let stream: grpcWeb.ClientReadableStream<LocationResponse> | null = null;

    if (locationSentSuccess) {
      setStreamStatus('connecting');
      setErrorMessage(null);

      console.log(`[${userId}] Location sent successfully. Starting stream subscription...`);

      // const client = new LocationServiceClient(EnvoyURL);

      const request = new UserIdRequest();
      request.setUserid(userId);
      const metadata = {};

      try {
        stream = client.subscribeLocations(request, metadata);
        console.log(`[${userId}] Stream object created.`);

        stream.on('data', (response: LocationResponse) => {
          const receivedUserId = response.getUserid();
          const location: LocationData = {
            latitude: response.getLatitude(),
            longitude: response.getLongitude(),
            timestamp: response.getTimestamp()
          };
          console.log(`[${userId}] Stream Data: From ${receivedUserId}, Loc: (${location.latitude.toFixed(6)}, ${location.longitude.toFixed(6)})`);

          setStreamStatus('connected');
          setErrorMessage(null);

          if (receivedUserId !== userId) {
            setOtherLocations(prev => ({
              ...prev,
              [receivedUserId]: location
            }));
          }
        });

        stream.on('end', () => {
          console.log(`[${userId}] Stream ended.`);
          setStreamStatus(prev => prev === 'connected' ? 'ended' : prev);
          stream = null;
        });

        stream.on('error', (err: grpcWeb.RpcError) => {
          console.error(`[${userId}] Stream Error: Code ${err.code}, Message: ${err.message}`);
          setErrorMessage(`Stream error: ${err.message} (Code: ${err.code})`);
          setStreamStatus('error');
          stream = null;
        });

        stream.on('status', (status: grpcWeb.Status) => {
          console.log(`[${userId}] Stream Status: Code ${status.code}, Details: ${status.details}`);
          if (status.code !== grpcWeb.StatusCode.OK) {
            setErrorMessage(`Stream status finish error: ${status.details} (Code: ${status.code})`);
            setStreamStatus('error');
          } else {
            setStreamStatus('ended');
          }
          stream = null;
        });

        return () => {
          console.log(`[${userId}] Cleaning up stream effect.`);
          if (stream) {
            console.log(`[${userId}] Cancelling stream.`);
            stream.cancel();
          }
        };

      } catch (error: any) {
        console.error(`[${userId}] Stream Setup Error: ${error.message}`);
        setErrorMessage(`Failed to initialize stream: ${error.message}`);
        setStreamStatus('error');
        return () => { console.log(`[${userId}] Cleanup for failed stream setup.`); };
      }
    } else {
      console.log(`[${userId}] Stream effect triggered but locationSentSuccess is false. No stream started.`);
    }

    return () => {};

  }, [locationSentSuccess, userId]);

  return (
      <div style={{ padding: '20px', fontFamily: 'sans-serif' }}>
        <h1>Simple gRPC-Web Location Tracker</h1>

        <div style={{ border: '1px solid #ccc', padding: '15px', marginBottom: '20px' }}>
          <h2>User Info & Status</h2>
          <p><strong>My ID:</strong> {userId}</p>
          <p>
            <strong>Stream Status (Receiving):</strong>
            <span style={{ marginLeft: '10px', color: streamStatus === 'connected' ? 'green' : streamStatus === 'connecting' ? 'orange' : streamStatus === 'idle' ? 'gray' : 'red' }}>
              {streamStatus === 'connected' ? 'Connected' :
                  streamStatus === 'connecting' ? 'Connecting...' :
                      streamStatus === 'ended' ? 'Ended' :
                          streamStatus === 'idle' ? 'Idle (Send location first)' : 'Error'}
            </span>
          </p>
          {errorMessage && (
              <p style={{ color: 'red', marginTop: '10px' }}>Error: {errorMessage}</p>
          )}
        </div>

        <div style={{ border: '1px solid #ccc', padding: '15px', marginBottom: '20px' }}>
          <h2>My Location (Sending)</h2>
          <button
              onClick={handleSendLocation}
              disabled={locationSentSuccess && streamStatus !== 'error'}
              style={{ padding: '8px 15px', cursor: locationSentSuccess ? 'not-allowed' : 'pointer' }}
          >
            {locationSentSuccess ? 'Location Sent! (Stream Starting)' : 'Send My Location'}
          </button>

          {myLocation && (
              <div style={{ marginTop: '15px', border: '1px solid #eee', padding: '10px' }}>
                <p><strong>Last Sent:</strong></p>
                <p>Latitude: {myLocation.latitude.toFixed(6)}, Longitude: {myLocation.longitude.toFixed(6)}</p>
                <p style={{ fontSize: '0.9em', color: '#555' }}>Time: {new Date(myLocation.timestamp).toLocaleTimeString()} ({myLocation.timestamp})</p>
              </div>
          )}
        </div>

        <div style={{ border: '1px solid #ccc', padding: '15px' }}>
          <h2>Other Users' Locations (Receiving)</h2>
          {streamStatus === 'idle' && <p style={{ color: '#888' }}>Send your location first to start receiving updates.</p>}
          {streamStatus !== 'idle' && Object.keys(otherLocations).length === 0 && (
              <p style={{ color: '#888', fontStyle: 'italic' }}>Waiting for location data from other users...</p>
          )}
          {Object.keys(otherLocations).length > 0 && (
              <ul style={{ listStyle: 'none', padding: 0 }}>
                {Object.entries(otherLocations)
                    .filter(([uid, loc]) => uid !== userId)
                    .map(([uid, loc]) => (
                        <li key={uid} style={{ borderBottom: '1px solid #eee', padding: '10px 0' }}>
                          <p><strong>{uid}:</strong></p>
                          <p>Latitude: {loc.latitude.toFixed(6)}, Longitude: {loc.longitude.toFixed(6)}</p>
                          <p style={{ fontSize: '0.9em', color: '#555' }}>Time: {new Date(loc.timestamp).toLocaleTimeString()} ({loc.timestamp})</p>
                        </li>
                    ))
                }
              </ul>
          )}
        </div>
      </div>
  );
}

export default App;