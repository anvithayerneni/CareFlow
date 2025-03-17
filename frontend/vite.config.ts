import { defineConfig } from 'vitest/config';
import react from '@vitejs/plugin-react';
export default defineConfig({ plugins:[react()], server:{proxy:{'/api':'http://localhost:8080'}}, test:{environment:'jsdom',globals:true,setupFiles:'./src/test-setup.ts'},build:{rollupOptions:{output:{manualChunks(id){if(id.includes('/node_modules/recharts/'))return 'charts';if(id.includes('/node_modules/'))return 'vendor';}}}} });
