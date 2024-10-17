import { Httpx } from 'https://jslib.k6.io/httpx/0.0.6/index.js';
import { describe, expect } from 'https://jslib.k6.io/k6chaijs/4.3.4.3/index.js';
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { check } from 'k6';


export function handleSummary(data) {
    return {
        "summary.html": htmlReport(data),
    };
}

export const options = {
    vus: 1,
    duration: '1m',
    thresholds: {
        http_req_duration: ['p(95)<1000'], //95% should return less than 1s
        http_req_failed: ['rate<0.10'] //10% of requests can fail - after some iterations the error 500 begin to happen
    }
}

let session = new Httpx({ baseURL: 'http://localhost:8080/api/v3' });

//Create an user
function createUserAndCheck() {
    describe('[User service] Create a new user', () => {
        const url = '/user'
        const payload = JSON.stringify({
            id: Math.floor(Math.random() * 100),
            username: "theUser",
            firstName: "TestJohn",
            lastName: "James",
            email: "test@email.com",
            password: "12345",
            phone: "12345",
            userStatus: 1
        })

        const headers = {
            'headers': {
                'Content-Type': 'application/json'
            }
        }

        const res = session.post(url, payload, headers)

        check(res, {
            'status should be 200': (r) => r.status === 200
        })
        session.newUsername = res.json('username');
        session.password = res.json('password');
    });

    describe('[User service] Fetch user created', () => {
        let res = session.get(`/user/${session.newUsername}`);

        expect(res.status, 'response status').to.equal(200);
        expect(res.json().username).to.equal(`${session.newUsername}`);
    });

}

function DoLogin() {
    describe('[User service] User logging', () => {
        const res = session.get(`/user/login?username=${session.newUsername}&password=${session.password}`)

        check(res, {
            'status should be 200': (r) => r.status === 200
        })
    });
}

function AddNewPetAndCheck() {
    describe('[Pet service] Add a new pet', () => {
        const url = '/pet'
        const payload = JSON.stringify(
            {
                "id": Math.floor(Math.random() * 5000),
                "name": "doggie",
                "category": {
                    "id": 1,
                    "name": "Dogs"
                },
                "photoUrls": [
                    "string"
                ],
                "tags": [
                    {
                        "id": 0,
                        "name": "string"
                    }
                ],
                "status": "available"
            })

        const headers = {
            'headers': {
                'Content-Type': 'application/json'
            }
        }

        const res = session.post(url, payload, headers)

        check(res, {
            'status should be 200': (r) => r.status === 200
        })

        session.petId = res.json('id');
    });

    describe('[Pet service] Fetch pet created', () => {
        let res = session.get(`/pet/${session.petId}`);

        expect(res.status, 'response status').to.equal(200);
        expect(res.json().id).to.equal(+session.petId);
    });

}

function PlaceOrderAndChecks() {
    describe('[Store service] Place an order', () => {
        const url = '/store/order'
        const payload = JSON.stringify({
            id: Math.floor(Math.random() * 5000),
            petId: `${session.petId}`,
            quantity: 10,
            shipDate: "2024-10-30T13:29:58.646Z",
            status: "approved",
            complete: true
        })

        const headers = {
            'headers': {
                'Content-Type': 'application/json'
            }
        }

        const res = session.post(url, payload, headers)

        check(res, {
            'status should be 200': (r) => r.status === 200
        })

        session.orderId = res.json('id');
    });

    describe('[Store service] Fetch order created', () => {
        let res = session.get(`/store/order/${session.orderId}`);

        expect(res.status, 'response status').to.equal(200);
        expect(res.json().id).to.equal(+session.orderId);
    });
}

function DoLogout() {
    describe('[User service] User logging out', () => {
        const res = session.get('/user/logout')

        check(res, {
            'status should be 200': (r) => r.status === 200
        })
    });
}

export default function testSuite() {
    createUserAndCheck();
    DoLogin();
    AddNewPetAndCheck();
    PlaceOrderAndChecks();
    DoLogout();
}