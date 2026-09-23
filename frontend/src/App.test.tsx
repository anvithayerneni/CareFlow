import { render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { BrowserRouter } from 'react-router-dom';
import { describe, expect, it, vi } from 'vitest';
import App from './App';
vi.mock('./api',()=>({api:{post:vi.fn().mockResolvedValue({data:{accessToken:'test-token'}})},setAccessToken:vi.fn()}));
describe('CareFlow dashboard',()=>{it('labels the synthetic demo and renders the patient overview',()=>{render(<QueryClientProvider client={new QueryClient()}><BrowserRouter><App/></BrowserRouter></QueryClientProvider>);expect(screen.getByText('DEMO ENVIRONMENT')).toBeInTheDocument();expect(screen.getByText('Good morning, Sophie')).toBeInTheDocument();expect(screen.getByText('Upcoming appointments')).toBeInTheDocument();});});
