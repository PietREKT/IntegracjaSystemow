import axios from 'axios'
import { useAuth } from './useAuth.js'

// Pomocnicza funkcja do nagłówków
function getAuthHeaders() {
    const { token } = useAuth()
    return token.value
        ? { headers: { Authorization: `Bearer ${token.value}` } }
        : {} // brak nagłówków jeśli niezalogowany
}

export async function addHouse(houseData) {
    const { token } = useAuth()

    const response = await axios.post(
        'http://localhost:8080/api/houses/add',
        houseData,
        {
            headers: {
                Authorization: `Bearer ${token.value}`,
            },
        }
    )

    return response.data
}

export async function getAvgPricesByYear() {
    const response = await axios.get(
        'http://localhost:8080/api/houses/stats/price',
        getAuthHeaders()
    )
    console.log(response.data)
    return response.data
}

export async function getTransactionsPerYear() {
    const response = await axios.get(
        'http://localhost:8080/api/houses/stats/count',
        getAuthHeaders()
    )
    return response.data
}

export async function getAvgPriceByCity() {
    const response = await axios.get(
        'http://localhost:8080/api/houses/stats/cities',
        getAuthHeaders()
    )
    return response.data
}


